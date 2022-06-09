package application;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static final String countryCodePath = "D:\\Onrising\\src\\main\\resources\\coutryCodes.txt";
    private static final String REGEX = "[( \\-,.^?~=:`´)\\[a-zA-Z]";
    private static final String EMPTY = "";

    public static void main(String[] args) throws FileNotFoundException {
        var countryCodeList = readCountryCodeFile();
        readInputFile(args[0], countryCodeList);
    }

    private static List<CountryCode> readCountryCodeFile() {
        List<CountryCode> countryCodeList = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(countryCodePath));
            String line;
            while ((line = in.readLine()) != null) {
                String[] vals = line.split("-");
                var country = vals[0];
                var code = Integer.parseInt(vals[1]);
                var countryCode = new CountryCode(country, code);
                countryCodeList.add(countryCode);
            }
            return countryCodeList;
        } catch (Exception e) {
            e.printStackTrace();
            return countryCodeList;
        }
    }

    private static void readInputFile(String arg, List<CountryCode> countryCodeList) {
        List<CountryCode> countryCodeList1 = new ArrayList<>();
        try {
            FileReader arq = new FileReader(arg);
            BufferedReader bf = new BufferedReader(arq);
            String line;
            while ((line = bf.readLine()) != null) {
                var number = line.replaceAll(REGEX, EMPTY);
                var countryCode = validarNumero(number, countryCodeList);
                if(countryCode != null) {
                    countryCodeList1.add(countryCode);
                }
            }
            countryCodeList1.stream().distinct().collect(Collectors.toList()).forEach(a -> {
                var count = countryCodeList1.stream()
                        .collect(Collectors.groupingBy(CountryCode::getCode,
                                LinkedHashMap::new,
                                Collectors.counting()));
                System.out.println(a.getCountry() + ": " + count.get(a.getCode()));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getCountryCode(String number) throws NumberParseException {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse("+" + number, null);
        return phoneNumber.getCountryCode();
    }

    private static CountryCode validarNumero(String number, List<CountryCode> countryCodeList) throws NumberParseException {
        CountryCode countryCode = null;
        if (number.length() >= 4 && number.length() <= 6) {
            if (!number.startsWith("0") && !number.contains(" ")) {
                countryCode = new CountryCode();
                countryCode.setCountry("Portugal");
                countryCode.setCode(351);
            }
        } else if (number.startsWith("+") && number.charAt(1) != ' ') {
            var finalNumber = number.replace("+", "");
            countryCode = getValor(countryCodeList, finalNumber);
        } else if (number.startsWith("00") && number.charAt(2) != ' ') {
            var finalNumber = number.replace("00", "");
            countryCode = getValor(countryCodeList, finalNumber);
        }
        return countryCode;
    }

    private static CountryCode getValor(List<CountryCode> countryCodeList, String finalNumber) throws NumberParseException {
        CountryCode countryCode = null;
        if (finalNumber.length() >= 9 && finalNumber.length() <= 14) {
            var code = getCountryCode(finalNumber);
            countryCode = validateCountryCode(countryCodeList, code);
        }
        return countryCode;
    }

    private static CountryCode validateCountryCode(List<CountryCode> countryCodeList, int code) {
       return countryCodeList.parallelStream()
                .filter(c -> c.getCode().equals(code)).findFirst().map(a -> new CountryCode(a.getCountry(), a.getCode())).orElse(null);
    }
}
