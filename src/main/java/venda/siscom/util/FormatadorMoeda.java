package venda.siscom.util;

import java.text.NumberFormat;
import java.util.Locale;

public final class FormatadorMoeda {

    private static final Locale LOCALE_BR = Locale.forLanguageTag("pt-BR");
    private static final NumberFormat MOEDA =
            NumberFormat.getCurrencyInstance(LOCALE_BR);

    private FormatadorMoeda() {
    }

    public static String formatar(Double valor) {
        return MOEDA.format(valor == null ? 0.0 : valor);
    }

    public static String formatar(double valor) {
        return MOEDA.format(valor);
    }

    public static double converter(String texto) {
        if (texto == null) {
            return 0.0;
        }

        String valor = texto
                .replace("R$", "")
                .replace("\u00A0", "")
                .trim();

        if (valor.isEmpty()) {
            return 0.0;
        }

        valor = valor.replace(" ", "");

        if (valor.contains(",")) {
            valor = valor.replace(".", "").replace(",", ".");
        } else {
            int primeiroPonto = valor.indexOf('.');
            int ultimoPonto = valor.lastIndexOf('.');

            if (primeiroPonto != ultimoPonto) {
                valor = valor.replace(".", "");
            }
        }

        return Double.parseDouble(valor);
    }
}
