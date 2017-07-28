#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <wiringPi.h>
#include <wiringSerial.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>
#include <unistd.h>
#include <termios.h>
#include <fcntl.h>
#include <curl/curl.h>

char charRecebido;
int fd;

int check_ch(char c);

PI_THREAD(recebe_serial){
  (void) piHiPri(10);

  char temp;
  char str1[10] = "";
  char val1[10] = "";
  char val2[10] = "";
  while(1) {
    charRecebido = serialGetchar(fd);
    
    if(charRecebido == '!'){
      strcpy(val1, str1);
      strcpy(str1, "");
    }
    else if(charRecebido == '#'){
      strcpy(val2, str1);
      strcpy(str1, "");

      if(strcmp(val1, "") != 0){
        time_t t = time(NULL);
        struct tm tm = *localtime(&t);

        char y[4];
        char m[2];
        char d[2];
        char h[2];
        char mi[2];
        char s[2];

        sprintf(y, "%d", tm.tm_year + 1900);

        if(tm.tm_mon < 9) sprintf(m, "0%d", tm.tm_mon + 1);
        else sprintf(m, "%d", tm.tm_mon + 1);

        if(tm.tm_mday < 10) sprintf(d, "0%d", tm.tm_mday);
        else sprintf(d, "%d", tm.tm_mday);

        if(tm.tm_hour < 10) sprintf(h, "0%d", tm.tm_hour);
        else sprintf(h, "%d", tm.tm_hour);

        if(tm.tm_min < 10) sprintf(mi, "0%d", tm.tm_min);
        else sprintf(mi, "%d", tm.tm_min);

        if(tm.tm_sec < 10) sprintf(s, "0%d", tm.tm_sec);
        else sprintf(s, "%d", tm.tm_sec);

        char datahora[26];

        sprintf(datahora, "%s-%s-%s&field2=%s:%s:%s", y, m, d, h, mi, s);

        CURL *curl;
        CURLcode res;

        curl_global_init(CURL_GLOBAL_ALL);

        curl = curl_easy_init();
        if(curl) {
          char dados[300] = "api_key=7SWZ408HLJJNB8D3&field1=";
          strcat(dados, datahora);
          strcat(dados, "&field3=");
          strcat(dados, val1);
          strcat(dados, "&field4=");
          strcat(dados, val2);

          curl_easy_setopt(curl, CURLOPT_URL, "https://api.thingspeak.com/update.json");
          curl_easy_setopt(curl, CURLOPT_POSTFIELDS, dados);

          res = curl_easy_perform(curl);
          if(res != CURLE_OK){
            fprintf(stderr, "curl_easy_perform() failed: %s\n", curl_easy_strerror(res));
            digitalWrite(4,LOW);
            digitalWrite(5,HIGH);
            
            char comm[200];
            strcpy(comm, "");
            sprintf(comm, "echo \"%s\" >> log.txt", dados);
            
            system(comm);
          }
          else{
            digitalWrite(5,LOW);
            digitalWrite(4,HIGH);
          }
          curl_easy_cleanup(curl);
        }
        curl_global_cleanup();
      }

      strcpy(val1, "");
      strcpy(val2, "");
    }
    else if(check_ch(charRecebido)){
      temp = charRecebido;
      char str2[3];
      sprintf(str2, "%c", temp);
      strcat(str1, str2);
    }

    fflush(stdout);
  }
}

int check_ch(char c){
  if(c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9'){
    return 1;
  }
  else{
    return 0;
  }
}

int main(void){
  if ((fd = serialOpen("/dev/ttyAMA0", 19200)) < 0){
    fprintf(stderr, "Unable to open serial device: %s\n", strerror(errno));
    return 1;
  }

  wiringPiSetup();
  pinMode(4, OUTPUT);
  pinMode(5, OUTPUT);

  digitalWrite(4, LOW);
  digitalWrite(5, LOW);

  piThreadCreate(recebe_serial);

  while(1){}

  return 0;
}
