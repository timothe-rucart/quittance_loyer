package org.example.service;

import org.example.config.ParametresQuittance;
import org.example.utils.ConvertToWords;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class FileTxtGenerator {

    private Map<String, String> params;
    private final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void generateOutFile(String[] args, String file, String fileOutTxt) {
        Path path = Paths.get(file);
        Path pathout = Paths.get(fileOutTxt);
        Stream<String> lines;
        params = getParams(args);

        try {
            lines = Files.lines(path, StandardCharsets.UTF_8);
            List<String> replacedLines = lines.map(this::replaceTag).toList();
            Files.write(pathout, replacedLines, StandardCharsets.UTF_8);
            lines.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String replaceTag(String line) {
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (line.contains(entry.getKey())) {
                line = line.replace(entry.getKey(), entry.getValue());
            }
        }
        return line;
    }

    private Map<String, String> getParams(String[] args) {

        LocalDate currentDate = LocalDate.now();
        LocalDate startDateQuittance = YearMonth.now().atDay(1);
        LocalDate endDateQuittance = YearMonth.now().atEndOfMonth();

        var paramMap = new HashMap<String, String>();
        paramMap.put(ParametresQuittance.dateDebutQuittance.getValue(), startDateQuittance.format(formatterDate));
        paramMap.put(ParametresQuittance.dateFinQuittance.getValue(), endDateQuittance.format(formatterDate));
        paramMap.put(ParametresQuittance.dateSignature.getValue(), currentDate.format(formatterDate));

        for (int i = 1; i < args.length; i++) {
            try {
                String[] currentArg = args[i].split(":");
                paramMap.put(ParametresQuittance.valueOf(currentArg[0]).getValue(), currentArg[1]);
            } catch (Exception e) {
                System.out.println("Erreur lors de la récupération du parametre " + args[i]);
            }
        }

        addDetailLoyer(paramMap);
        return paramMap;
    }

    private void addDetailLoyer(Map<String, String> paramsMap) {

        Long charges = Long.parseLong(paramsMap.get(ParametresQuittance.chargesChiffre.getValue()));
        Long loyer = Long.parseLong(paramsMap.get(ParametresQuittance.loyerChiffre.getValue()));
        Long loyerCC = charges + loyer;

        paramsMap.put(ParametresQuittance.loyerChiffre.getValue(), loyer.toString());
        paramsMap.put(ParametresQuittance.chargesChiffre.getValue(), charges.toString());
        paramsMap.put(ParametresQuittance.loyerCCChiffre.getValue(), loyerCC.toString());

        paramsMap.put(ParametresQuittance.loyerLettre.getValue(), ConvertToWords.convert(loyer));
        paramsMap.put(ParametresQuittance.chargesLettre.getValue(), ConvertToWords.convert(charges));
        paramsMap.put(ParametresQuittance.loyerCCLettre.getValue(), ConvertToWords.convert(loyerCC));

    }
}
