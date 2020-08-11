package org.domainspecific.dsl.trading;

import org.domainspecific.dsl.trading.external.Parser;
import org.domainspecific.dsl.trading.internal.Trading;

import java.util.Scanner;

public class ExternalTradingApp {
    public static void main(String[] args) {

        /*
        buy <<Unit>> <<StockSymbol>> stocks at <<Price>>
         */

        Parser p = new Parser(Parser.keyWords);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            Trading.Order o = p.parse(line);
            System.out.println(o);
        }
    }

}
