package org.example.utils;

import java.text.DecimalFormat;

public class ConvertToWords {
    private static final String[] dizaineNames = {
            "",
            "",
            "vingt",
            "trente",
            "quarante",
            "cinquante",
            "soixante",
            "soixante",
            "quatre-vingt",
            "quatre-vingt"
    };

    private static final String[] uniteNames1 = {
            "",
            "un",
            "deux",
            "trois",
            "quatre",
            "cinq",
            "six",
            "sept",
            "huit",
            "neuf",
            "dix",
            "onze",
            "douze",
            "treize",
            "quatorze",
            "quinze",
            "seize",
            "dix-sept",
            "dix-huit",
            "dix-neuf"
    };

    private static final String[] uniteNames2 = {
            "",
            "",
            "deux",
            "trois",
            "quatre",
            "cinq",
            "six",
            "sept",
            "huit",
            "neuf",
            "dix"
    };

    static String convertZeroToHundred(int number) {

        int laDizaine = number / 10;
        int lUnite = number % 10;
        String resultat = "";

        switch (laDizaine) {
            case 1:
            case 7:
            case 9:
                lUnite = lUnite + 10;
                break;
            default:
        }

        String laLiaison = "";
        if (laDizaine > 1) {
            laLiaison = "-";
        }
        switch (lUnite) {
            case 0:
                laLiaison = "";
                break;
            case 1:
                if (laDizaine == 8) {
                    laLiaison = "-";
                } else {
                    laLiaison = " et ";
                }
                break;
            case 11:
                if (laDizaine == 7) {
                    laLiaison = " et ";
                }
                break;
            default:
        }

        switch (laDizaine) {
            case 0:
                resultat = uniteNames1[lUnite];
                break;
            case 8:
                if (lUnite == 0) {
                    resultat = dizaineNames[laDizaine];
                } else {
                    resultat = dizaineNames[laDizaine] + laLiaison + uniteNames1[lUnite];
                }
                break;
            default:
                resultat = dizaineNames[laDizaine] + laLiaison + uniteNames1[lUnite];
        }
        return resultat;
    }

    private static String convertLessThanOneThousand(int number) {

        int lesCentaines = number / 100;
        int leReste = number % 100;
        String sReste = convertZeroToHundred(leReste);

        String resultat;
        switch (lesCentaines) {
            case 0:
                resultat = sReste;
                break;
            case 1:
                if (leReste > 0) {
                    resultat = "cent " + sReste;
                } else {
                    resultat = "cent";
                }
                break;
            default:
                if (leReste > 0) {
                    resultat = uniteNames2[lesCentaines] + " cent " + sReste;
                } else {
                    resultat = uniteNames2[lesCentaines] + " cents";
                }
        }
        return resultat;
    }

    public static String convert(long number) {
        if (number == 0) {
            return "zero";
        }

        String mask = "000000";
        DecimalFormat df = new DecimalFormat(mask);
        String snumber = df.format(number);

        int lesCentMille = Integer.parseInt(snumber.substring(0, 3));
        int lesMille = Integer.parseInt(snumber.substring(3, 6));

        String tradCentMille;
        switch (lesCentMille) {
            case 0:
                tradCentMille = "";
                break;
            case 1:
                tradCentMille = "mille ";
                break;
            default:
                tradCentMille = convertLessThanOneThousand(lesCentMille) + " mille ";
        }
        return tradCentMille + convertLessThanOneThousand(lesMille);
    }
}
