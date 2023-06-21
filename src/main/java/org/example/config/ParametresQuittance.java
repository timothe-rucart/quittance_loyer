package org.example.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ParametresQuittance {

    loyerCCChiffre("XXloyerCCChiffreXX"),
    loyerCCLettre("XXloyerCCLettreXX"),
    loyerChiffre("XXloyerChiffreXX"),
    loyerLettre("XXloyerLettreXX"),
    chargesChiffre("XXchargesChiffreXX"),
    chargesLettre("XXchargesLettreXX"),
    dateDebutBail("XXdateDebutBailXX"),
    dateDebutQuittance("XXdateDebutXX"),
    dateFinQuittance("XXdateFinXX"),
    dateSignature("XXdateSignatureXX"),
    nom("XXnomXX");

    private final String value;
}
