#include <avr/io.h>
#include <util/delay.h>
#include <avr/interrupt.h>
#include <avr/wdt.h>
#include <stdbool.h>
#include "usbdrv/usbdrv.h"

#define SPEAKER PB0
#define LED PB1
#define abs(x) (((x) < 0) ? (-x) : (x))
#define USB_TONE_WRITE 17

#if F_CPU <= 1000000
  #define TONE_FREQUENCY_CUTOFF_5  (7)
  #define TONE_FREQUENCY_CUTOFF_4  (30)
  #define TONE_FREQUENCY_CUTOFF_3  (243)
  #define TONE_FREQUENCY_CUTOFF_2  (1949)
  #define TONE_FREQUENCY_CUTOFF_1  (65535)
#elif F_CPU <= 8000000
  #define TONE_FREQUENCY_CUTOFF_5  (60)
  #define TONE_FREQUENCY_CUTOFF_4  (243)
  #define TONE_FREQUENCY_CUTOFF_3  (1949)
  #define TONE_FREQUENCY_CUTOFF_2  (15594)
  #define TONE_FREQUENCY_CUTOFF_1  (65535)
#elif F_CPU <= 16000000
  #define TONE_FREQUENCY_CUTOFF_5  (121)
  #define TONE_FREQUENCY_CUTOFF_4  (487)
  #define TONE_FREQUENCY_CUTOFF_3  (3898)
  #define TONE_FREQUENCY_CUTOFF_2  (31189)
  #define TONE_FREQUENCY_CUTOFF_1  (65535)
#elif F_CPU <= 16500000
  #define TONE_FREQUENCY_CUTOFF_5  (121)
  #define TONE_FREQUENCY_CUTOFF_4  (487)
  #define TONE_FREQUENCY_CUTOFF_3  (3898)
  #define TONE_FREQUENCY_CUTOFF_2  (31189)
  #define TONE_FREQUENCY_CUTOFF_1  (65535)
#endif  

#define TIMER0_PRESCALER_VALUE_1  (1)
#define TIMER0_PRESCALER_VALUE_2  (8)
#define TIMER0_PRESCALER_VALUE_3  (64)
#define TIMER0_PRESCALER_VALUE_4  (256)
#define TIMER0_PRESCALER_VALUE_5  (1024)

#define NOTE_B0  31
#define NOTE_C1  33
#define NOTE_CS1 35
#define NOTE_D1  37
#define NOTE_DS1 39
#define NOTE_E1  41
#define NOTE_F1  44
#define NOTE_FS1 46
#define NOTE_G1  49
#define NOTE_GS1 52
#define NOTE_A1  55
#define NOTE_AS1 58
#define NOTE_B1  62
#define NOTE_C2  65
#define NOTE_CS2 69
#define NOTE_D2  73
#define NOTE_DS2 78
#define NOTE_E2  82
#define NOTE_F2  87
#define NOTE_FS2 93
#define NOTE_G2  98
#define NOTE_GS2 104
#define NOTE_A2  110
#define NOTE_AS2 117
#define NOTE_B2  123
#define NOTE_C3  131
#define NOTE_CS3 139
#define NOTE_D3  147
#define NOTE_DS3 156
#define NOTE_E3  165
#define NOTE_F3  175
#define NOTE_FS3 185
#define NOTE_G3  196
#define NOTE_GS3 208
#define NOTE_A3  220
#define NOTE_AS3 233
#define NOTE_B3  247
#define NOTE_C4  262
#define NOTE_CS4 277
#define NOTE_D4  294
#define NOTE_DS4 311
#define NOTE_E4  330
#define NOTE_F4  349
#define NOTE_FS4 370
#define NOTE_G4  392
#define NOTE_GS4 415
#define NOTE_A4  440
#define NOTE_AS4 466
#define NOTE_B4  494
#define NOTE_C5  523
#define NOTE_CS5 554
#define NOTE_D5  587
#define NOTE_DS5 622
#define NOTE_E5  659
#define NOTE_F5  698
#define NOTE_FS5 740
#define NOTE_G5  784
#define NOTE_GS5 831
#define NOTE_A5  880
#define NOTE_AS5 932
#define NOTE_B5  988
#define NOTE_C6  1047
#define NOTE_CS6 1109
#define NOTE_D6  1175
#define NOTE_DS6 1245
#define NOTE_E6  1319
#define NOTE_F6  1397
#define NOTE_FS6 1480
#define NOTE_G6  1568
#define NOTE_GS6 1661
#define NOTE_A6  1760
#define NOTE_AS6 1865
#define NOTE_B6  1976
#define NOTE_C7  2093
#define NOTE_CS7 2217
#define NOTE_D7  2349
#define NOTE_DS7 2489
#define NOTE_E7  2637
#define NOTE_F7  2794
#define NOTE_FS7 2960
#define NOTE_G7  3136
#define NOTE_GS7 3322
#define NOTE_A7  3520
#define NOTE_AS7 3729
#define NOTE_B7  3951
#define NOTE_C8  4186
#define NOTE_CS8 4435
#define NOTE_D8  4699
#define NOTE_DS8 4978

volatile bool flag = false;
uint32_t delay_loop;
uint32_t loop_const;
uint16_t clock_select;
volatile uint16_t frequency = NOTE_A4;
volatile uint16_t time_period = UINT16_MAX;
volatile uint32_t iterations = UINT32_MAX;

void delay(uint32_t milliseconds)
{
	for(delay_loop = 0; delay_loop < (milliseconds / 10); delay_loop = delay_loop + 1)
	{
		_delay_ms(10);
        wdt_reset();
	}
}

void tone()
{
    GTCCR = GTCCR | (1 << TSM) | (1 << PSR0);
    TCCR0A = (1 << WGM01) | (1 << COM0A0);
    TIMSK = (1 << OCIE0A); 

    if(frequency <= TONE_FREQUENCY_CUTOFF_5)
    {
        clock_select = TIMER0_PRESCALER_VALUE_5;
        TCCR0B = (1 << CS02) | (0 << CS01) | (1 << CS00);
    }
    else if(frequency <= TONE_FREQUENCY_CUTOFF_4)
    {
        clock_select = TIMER0_PRESCALER_VALUE_4;
        TCCR0B = (1 << CS02) | (0 << CS01) | (0 << CS00);
    }
    else if(frequency <= TONE_FREQUENCY_CUTOFF_3)
    {
        clock_select = TIMER0_PRESCALER_VALUE_3;
        TCCR0B = (0 << CS02) | (1 << CS01) | (1 << CS00);
    }
    else if(frequency <= TONE_FREQUENCY_CUTOFF_2)
    {
        clock_select = TIMER0_PRESCALER_VALUE_2;
        TCCR0B = (0 << CS02) | (1 << CS01) | (0 << CS00);
    }
    else if (frequency <= TONE_FREQUENCY_CUTOFF_1)
    {
        clock_select = TIMER0_PRESCALER_VALUE_1;
        TCCR0B = (0 << CS02) | (0 << CS01) | (1 << CS00);
    }
    else
    {
        frequency = 440;
    }    
    TCNT0 = 0;
    OCR0A = ((2L * F_CPU) / (frequency * 2L * clock_select) + 1L) / 2L - 1L;
    iterations = (2L * time_period * frequency) / 1000L;
    GTCCR = GTCCR & ~(1 << TSM);
}

void no_tone()
{
    GTCCR = GTCCR | (1 << TSM) | (1 << PSR0);
    TCCR0A = 0;
    TIMSK = 0; 
    TCNT0 = 0;
    GTCCR = GTCCR & ~(1 << TSM);
}

void calibrate(void)
{
    int target_length = (unsigned)(1499 * (double)F_CPU / 10.5e6 + 0.5);
    int trial_length;
    int trial_value;
    int best_value;
    int step;
    int lowest_deviation = INT16_MAX;

    for(loop_const = 0; loop_const < 1; loop_const = loop_const + 1)
    {
        trial_value = (loop_const == 0) ? 0 : 128;
        trial_length = 0;

        for(step = 64; step > 0; step = step >> 1)
        {
            if(trial_length < target_length)
            {
                trial_value = trial_value + step;
            }
            else
            {
                trial_value = trial_value - step;
            }

            OSCCAL = trial_value;
            trial_length = usbMeasureFrameLength();
            if(abs(target_length - trial_length) < lowest_deviation)
            {
                best_value = trial_value;
                lowest_deviation = abs(target_length - trial_length);
            }            
        }
    }

    
    for(loop_const = best_value - 1; loop_const <= best_value + 1; loop_const++)
    {
        OSCCAL = loop_const;
        trial_length = usbMeasureFrameLength();
        if(abs(target_length - trial_length) < lowest_deviation)
        {
            best_value = trial_value;
            lowest_deviation = abs(target_length - trial_length);
        }
    }

    OSCCAL = best_value;
}

void led_on()
{
	PORTB = PORTB | (1 << LED);
}

void led_off()
{
	PORTB = PORTB & ~(1 << LED);
}

ISR(TIMER0_COMPA_vect, ISR_NOBLOCK)
{
    if(iterations > 0)
    {
        iterations = iterations - 1;
    }
    else if(iterations == 0)
    {
        no_tone();
    }
}

USB_PUBLIC uchar usbFunctionSetup(uchar data[8]) 
{
	usbRequest_t* recv_data = (void*)data;
    led_on();
    delay(100);
    led_off();
    if(recv_data->bRequest == USB_TONE_WRITE)
    {
        frequency = recv_data->wValue.word;
        time_period = recv_data->wIndex.word;
    }
    tone();
    return 0;
}

int main(void)
{
    DDRB = DDRB | (1 << SPEAKER) | (1 << LED);
    tone();
    wdt_enable(WDTO_1S);
    usbInit();
    usbDeviceDisconnect();
    delay(500);
    usbDeviceConnect();
    sei();

    while(true)
    {
        wdt_reset();
        usbPoll();
    }
}    