decisionSystem("FX Transaction", tradeSchema, s -> {

            s.rule("High Transfer discount", condition -> {
                condition
                        .gt("amount", 1000d)
                        .eq("source", "SGD")
                        .eq("target", "INR")
                        .action((rule, row) -> row.setDiscount(2.0d));
            });

            s.rule("less than minimum", condition -> {
                condition
                        .lt("amount", 1000d)
                        .action((rule, row) -> row.setDiscount(-1.0d));
            });


            s.rule("limited time period", conditions -> {
                        conditions
                                .between("date", 20200805, 20200831)
                                .action((rule, row) -> row.setDiscount(5.0d));
                    }
            );

});