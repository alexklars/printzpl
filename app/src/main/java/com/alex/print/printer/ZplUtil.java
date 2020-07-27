package com.alex.print.printer;

import com.alex.print.repository.db.entity.Product;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ZplUtil {

    public static String getProductListZplStr(List<Product> productList) {
        StringBuilder zplStr = new StringBuilder();

        for (Product product : productList) {
            zplStr.append(getProductZPL(product));
        }
        return zplStr.toString();
    }

    private static String getProductZPL(Product product) {
        return "^XA" +

                "^CWT,E:TT0003M_.TTF" +
                "^CFT,25,25" +
                "^CI28" +

                "^FO 30,60" +
                "^FB 540,3,20,L,0" +
                "^FH^FD" + product.getName() + "^FS" +

                "^FO 30,150" +
                "^FDRoute " + product.getRoute() + "^FS" +

                "^FO 30,210" +
                "^FD#" + product.getOrder() + "^FS" +

                "^FO 30,270" +
                "^FH^FD" + product.getCourier() + "^FS" +

                "^FO 30,300" +
                "^FH^FD" + product.getSupplier() + "^FS" +

                "^FO 30,330" +
                "^FD" + getCurrDateStr() + "^FS" +

                "^FO 400,150" +
                "^BQN,2,5" +
                "^FDMA," + product.getGuid() + "^FS" +

                "^XZ";
    }

    static final SimpleDateFormat DATE_FORMAT_DD_MM_YYYY = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    public static String getCurrDateStr() {
        return DATE_FORMAT_DD_MM_YYYY.format(new Date());
    }
}
