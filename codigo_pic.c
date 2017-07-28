#include "SanUSB1X.h"

void interrupt interrupcao() {
}

unsigned long int resultado1, resultado2, Vresult1, Vresult2;
unsigned char i = 0;

void main() {
    clock_int_4MHz();
    taxa_serial(19200);
    habilita_canal_AD(AN0_a_AN1);

    while (1) {
        if (!entrada_pin_e3) {
            Reset();
        }

        resultado1 = 0;
        resultado2 = 0;
        
        for(i = 0; i < 30; i++){
            resultado1 += le_AD10bits(0);
            resultado2 += le_AD10bits(1);
            tempo_ms(1980);
        }
        
        resultado1 /= 30;
        Vresult1 = (resultado1 * 5000) / 1023;

        resultado2 /= 30;
        Vresult2 = (resultado2 * 5000) / 1023;

        sendnum(Vresult1);
        swputc('!');
        sendnum(Vresult2);
        swputc('#');
    }
}
