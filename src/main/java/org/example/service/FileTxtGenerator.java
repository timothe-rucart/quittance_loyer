package org.example.service;

import org.example.config.ParametresQuittance;
import org.example.utils.ConvertToWords;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${parameters-quittance.nomLocataire}")
    private String nomLocataire;
    @Value("${parameters-quittance.dateDebutBail}")
    private String dateDebutBail;
    @Value("${parameters-quittance.chargesChiffre}")
    private String chargesChiffre;
    @Value("${parameters-quittance.loyerChiffre}")
    private String loyerChiffre;

    public void generateOutFile(String file, String fileOutTxt) {
        var path = Paths.get(file);
        var pathout = Paths.get(fileOutTxt);
        Stream<String> lines;
        params = getParams();

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

    private Map<String, String> getParams() {

        var paramMap = new HashMap<String, String>();
        try {
            var currentDate = LocalDate.now();
            var startDateQuittance = YearMonth.now().atDay(1);
            var endDateQuittance = YearMonth.now().atEndOfMonth();


            paramMap.put(ParametresQuittance.dateDebutQuittance.getValue(), startDateQuittance.format(formatterDate));
            paramMap.put(ParametresQuittance.dateFinQuittance.getValue(), endDateQuittance.format(formatterDate));
            paramMap.put(ParametresQuittance.dateSignature.getValue(), currentDate.format(formatterDate));

            paramMap.put(ParametresQuittance.nom.getValue(), nomLocataire);
            paramMap.put(ParametresQuittance.dateDebutBail.getValue(), dateDebutBail);
            paramMap.put(ParametresQuittance.chargesChiffre.getValue(), chargesChiffre);
            paramMap.put(ParametresQuittance.loyerChiffre.getValue(), loyerChiffre);

            addDetailLoyer(paramMap);
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération d'un paramètre d'entrée, merci de se référer au README");
        }
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
