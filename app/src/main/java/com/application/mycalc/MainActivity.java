package com.application.mycalc;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends Activity  {

    String result = "0";                     // Результат в строковой форме
    char sign;                                                  // Выбраный знак (+,-,*,/)
    String a = "", b = "";                                      //Числа а и b в строковом виде
    boolean plus = false, minus = false, multiple = false, divide = false;        //Флаги выбора знака
    int textSize = 40, maxsize = 10;                            //размер текста, максимальный размер строки
    TextView tvResult;                                          //Объявляем текст вью
    boolean zeroTag = false;                                    //Флаг нулевого делителя




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tvResult = (TextView) findViewById(R.id.tvResult);           //Находим текст вью
        tvResult.setText(result);                                   //Вставляем исходный текст
        tvResult.setTextSize(textSize);                             //Задаём размер

    }


    public void onClick(View v) {

        double aDbl, bDbl;      //числа а и b в double виде
        double resultD=0;       // Результат в дабл форме


        char[] numbers = {'1', '2','3', '4','5','6','7','8','9'};
        int[] ids = {R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};

        //производим обработку нажатий на каждую из кнопок.

        for (int i = 0; i < 9; i++){                                //Прописываем действия для всех кнопок с циярами
            if (v.getId() == ids[i]){
                if (!(plus||minus||multiple||divide)) {             //Если еще не был выбран знак +-*/ (работаем с числом а)
                    a += numbers[i];
                }
                else {                                              //Иначе (работаем с числом б)
                    b += numbers[i];
                }
            }
        }

        switch (v.getId()){

            case R.id.btn0:
                if (!(plus||minus||multiple||divide)){          //Если еще не был выбран знак +-*/  (работаем с числом а)
                    if (!a.equals("0")) {                         //если а не равно 0
                        a = a + "0";                                    //добавляем к а 0
                    }
                }
                else                                            //Иначе  (работаем с числом б)
                    if (!b.equals("0")){                                 //если б не равно 0
                        b = b + "0";                                    //добавляем к б 0
                    }
                break;                                           //условия проверки на ноль сделано для того, чтобы нельзя было ввести несколько нулей подряд.


            //Oбработка нажатия на кнопку очистки:
            case R.id.btnClear:                                       //Зануляем все переменные
                a="";
                b="";
                sign=' ';
                result = "0";
                minus = plus = multiple = divide = false;

                ResetSize();

                break;

            // Обработка нажатия на кнопку смены знака
            case R.id.btnChangeSign:
                if(!a.equals(""))                                      //Если введено а (далее такой же смысл)
                    if (Double.parseDouble(a) % 1 == 0) {              //Если число целое (остаток при деление на 1 = 0)
                        a = ( - Long.parseLong(a))+"";                  //Парсим его как лонг, и вычитаем его из 0
                    }else                                              //Иначе (остаток не 0)
                        a = ( - Double.parseDouble(a))+"";             //Парсим его как Дабл и вычитаем его из 0
                else result = "0";                                     //Если а не было введено результат зануляем
                break;


            //Обработка нажатия на кнопку взятия процента от числа (я не особо разобрался что она должна делать, поэтому она просто делит число на 100)
            case R.id.btnPercent:
                if (!a.equals("")){
                    result = (Double.parseDouble(a) / 100) + "";               //основной функционал
                    a = result;                                                //Сохраняем результат в a для того, чтобы дальше с ним работать
                }
                else result = "0";

                ResetSize();                                              //Возвращаем размер текста в исходное состояние

                break;

            //Обработка нажатия на +-*/  (по аналогии все 4)
            case R.id.btnPlus:
                plus=true;                                              //Выбираем +
                sign='+';                                               //Переменной, хранящий знак присваиваем его соотвественно
                minus = multiple = divide = false;                      //зануляем все остальные знаки
                break;
            case R.id.btnMinus:
                minus = true;
                sign='-';
                plus = multiple = divide = false;
                break;
            case R.id.btnMultiply:
                multiple = true;
                sign='*';
                plus = minus = divide = false;
                break;
            case R.id.btnDivide:
                divide = true;
                sign='/';
                plus = minus = multiple = false;
                break;



                // Основная кнопка - РЕЗУЛЬТАТ
            case R.id.btnResult:
                if (!a.equals("") && !b.equals("")) {                      //Если не пустые значения чисел
                    aDbl = Double.parseDouble(a);                       //Парсим как дабл
                    bDbl = Double.parseDouble(b);


                    // ВЫЧИСЛЯЕМ РЕЗУЛЬТАТ и округляем его до (0-20) цифр после запятой
                    if (plus)     //Вычисление суммы
                        resultD = new BigDecimal(aDbl + bDbl).setScale(10, RoundingMode.HALF_UP).doubleValue();
                    if (minus)   //Вычисление разности
                        resultD = new BigDecimal(aDbl - bDbl).setScale(10, RoundingMode.HALF_UP).doubleValue();
                    if (multiple)  //Вычисление произведения
                        resultD = new BigDecimal(aDbl  * bDbl).setScale(10, RoundingMode.HALF_UP).doubleValue();
                    if (divide)   //Вычисление частного
                        if (bDbl!=0)         //Если только знаменатель не равен нулю
                             resultD = new BigDecimal(aDbl / bDbl).setScale(10, RoundingMode.HALF_UP).doubleValue();
                        else {
                            Toast.makeText(this, "На ноль делить нельзя", Toast.LENGTH_LONG).show();
                            zeroTag = true;                                                             //говорим, что встретили 0 в знаменателе
                        }


                    if (resultD % 1 == 0)                      //Если результат оказался целым
                        result = Math.round(resultD) + "";     //откидываем нули после запятой и делаем его Int
                    else result = resultD + "";                 //Иначе просто запоминаем

                    if (zeroTag)                                 //Если было деление на ноль, то сохраняем в результат значения числа, которое было поделено на 0
                        result = a;

                    ResetSize();                         // Возвращаем значение размера текста в исх. состояние

                    a = result;                                   //Запоминаем результат для дальнейшей работы с ним
                    b = "";                                        //Зануляем b
                }                //Конец условия "если числа не пустые" =>
                else {             // => Если числа пустые
                    result = "0";
                    a = b = "";
                    sign = ' ';                           //Зануляем всё
                    plus = false; minus=false; multiple=false; divide=false;
                    Toast.makeText(this, "Неверный ввод", Toast.LENGTH_LONG).show();
                }     //Говорим, что пользователь ошибся при вводе


                plus = minus = multiple = divide = false;              //зануляем все переменные выбора знака, чтобы произвести новые расчеты
                sign = ' ';                                                         //Зануляем сам знак
                zeroTag = false;                                                        //Зануляем Флаг нулевого делителя
                break;




            //Обработка нажатия на кнопку, устанавливающую точку
            case R.id.btnPoint:
                if (!(plus||minus||multiple||divide)){
                    if(!a.contains("."))                        //Если еще не поставлена точка в числе
                        a += ".";                                //Прибавляем точку к строке
                }
                else
                    if(!b.contains("."))                         //Если еще не поставлена точка в числе
                        b=b+".";                                 //Конкатенируем точку к строке
                break;

            //Обработка нажатия на кнопку вычисления корня
            case R.id.btnSqrt:
                if (!a.equals("")) {
                    aDbl = Double.parseDouble(a);                //Парсим строку как дабл (так как функция взятия корня работает с дабл)
                    result = Math.sqrt(aDbl)+"";                 //Вычисляем сам корень
                    aDbl = Double.parseDouble(result);           //Присваиваем результат как дабл
                    if (aDbl % 1 == 0)                           //Смотрим, целое ли число получилось
                        result = Math.round(aDbl)+"";            // Если целое, убераем нули после запятой с помощью функции round


                    if (result.equals("NaN")){                   //Если результат оказался NaN (Корень из отрицательного значения)
                        a="";                                    //Зануляем все переменные (чтобы устранить возможность работы пользователя со строкой, содержащей NaN
                        b="";
                        sign=' ';
                        minus = plus = multiple = divide = false;
                    }
                    else{                                         //Иначе, всё как обычно
                        a=result;
                        b="";
                    }

                }
                else result = "0";

                ResetSize();                                      //возвращаем значение размера строки в исходное состояние

                break;

        }                    // КОНЕЦ SWITCH

        if (result.equals("")){                                 //Если результат еще не сформирован, работаем с числами
            if ((a+sign+b).length() > maxsize) {                //Если длина строки больше максимальной, уменьшаем ее, увеличиваем максимум
                textSize -= 3;
                maxsize += 2;
                if (textSize > 17)                               //Порог уменьшения 17
                    tvResult.setTextSize(textSize);
            }
            tvResult.setText(a+sign+b);}                         //Присваиваем строку текст вью
        else {                                                   //Аналогично с результатом
            if (result.length() > maxsize) {
                textSize -= 3;
                maxsize +=2;
                if (textSize>17)
                    tvResult.setTextSize(textSize);
            }
            tvResult.setText(result);
        }

        result = "";
    }



    protected void onSaveInstanceState(Bundle outState) {      //Сохраняем нужные значения для того, чтобы они сохранились после поворота экрана
        super.onSaveInstanceState(outState);
        outState.putString("a",a);
        outState.putString("b",b);
        outState.putString("res", result);
        outState.putChar("sign", sign);
        outState.putBoolean("plus", plus);
        outState.putBoolean("minus", minus);
        outState.putBoolean("multiply", multiple);
        outState.putBoolean("divide", divide);
        outState.putInt("textSize", textSize);
        outState.putInt("maxTextSize", maxsize);
    }



    protected void onRestoreInstanceState(Bundle savedInstanceState) {     //Загружаем значения после поворота экрана
        super.onRestoreInstanceState(savedInstanceState);
        a = savedInstanceState.getString("a");
        b = savedInstanceState.getString("b");
        result = savedInstanceState.getString("res");
        sign = savedInstanceState.getChar("sign");
        plus = savedInstanceState.getBoolean("plus");
        minus = savedInstanceState.getBoolean("minus");
        multiple = savedInstanceState.getBoolean("multiply");
        divide = savedInstanceState.getBoolean("divide");
        textSize = savedInstanceState.getInt("textSize");
        maxsize = savedInstanceState.getInt("maxTextSize");

        if (result.equals(""))
            tvResult.setText(a+sign+b);                                 //Вставляем прежний текст
        else
            tvResult.setText(result);
        tvResult.setTextSize(textSize);                                  //Делаем прежний размер строки
    }


    public void ResetSize() {
        if (result.length()<11) {                   // Возвращаем значение размера текста в исх. состояние
            textSize=40;
            maxsize=10;
            tvResult.setTextSize(textSize);
        }
    }
}
