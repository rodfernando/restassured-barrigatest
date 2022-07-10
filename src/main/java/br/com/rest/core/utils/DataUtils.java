package br.com.rest.core.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataUtils {

    public static String getDataDiferencaDias(Integer qtdDias) { //qtdDias dirá a a diferença de dias
        Calendar cal = Calendar.getInstance(); //retorna o dia atual
        cal.add(Calendar.DAY_OF_MONTH, qtdDias); //valor positivo: futuro; valor negativo: passado
        return getDataFormatada(cal.getTime());
    }

    //formatar em string
    public static String getDataFormatada(Date data) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(data);
    }
}
