package com.kd.companyservice.test;


import org.junit.Test;

public class ExampleTest {

  // write smart code to convert the hindi text in english
  public String convertHindiToEnglish(String hindiText) {

    hindiText = hindiText.replace("हा", "ha");
    hindiText = hindiText.replace("हि", "hi");
    hindiText = hindiText.replace("ही", "hi");
    hindiText = hindiText.replace("हु", "hu");
    hindiText = hindiText.replace("हे", "he");
    hindiText = hindiText.replace('ह', 'h');
    return hindiText;
  }

  // write test for above function
  @Test
  public void convertHindiToEnglishTest() {
    String hindiText = "हा हि ही हु हे ह";
    System.out.println(convertHindiToEnglish(hindiText));
  }
}
