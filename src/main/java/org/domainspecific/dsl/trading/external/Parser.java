package org.domainspecific.dsl.trading.external;

import org.domainspecific.dsl.trading.internal.Trading.Order;
import org.domainspecific.dsl.trading.external.rule.AtParsingRule;
import org.domainspecific.dsl.trading.external.rule.BuyParsingRule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Parser {
    public static Set<String> keyWords = new HashSet<String>() {{
        add("buy");
        add("at");
    }};
    private final ArrayList<ParsingRule<Order>> rule;

    public Parser(Set<String> keyWords) {
        this.keyWords = keyWords;
        this.rule = new ArrayList<ParsingRule<Order>>() {{
            add(new BuyParsingRule());
            add(new AtParsingRule());
        }};
    }


    public Order parse(String line) {
        ParserArgs<Order> args = new ParserArgs();

        for (String token : line.split(" ")) {

            if (keyWords.contains(token)) {
                args.keyWord = token;
            } else {
                args.buffer.add(token);
            }

            Optional<ParsingRule<Order>> q = rule.stream()
                    .filter(rule -> rule.match(args.keyWord))
                    .findFirst();
            q.ifPresent(rule -> rule.apply(args));

            if (args.completed)
                return args.value;
        }
        return null;
    }
}
