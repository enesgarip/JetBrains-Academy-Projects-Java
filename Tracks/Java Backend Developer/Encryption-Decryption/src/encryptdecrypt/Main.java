package encryptdecrypt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;


class Cryptography {
    private final CryptographyAlgorithm algorithm;

    public Cryptography(CryptographyAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public StringBuilder encrypt(String data, int key) {
        return this.algorithm.enc(data, key);
    }

    public StringBuilder decrypt(String data, int key) {
        return this.algorithm.dec(data, key);
    }
}

interface CryptographyAlgorithm {

    StringBuilder enc(String data, int key);

    StringBuilder dec(String data, int key);
}

class ShiftingAlgorithm implements CryptographyAlgorithm {

    public StringBuilder enc(String data, int key) {
        StringBuilder cypherData = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            char temp = data.charAt(i);
            if (Character.isLetter(temp)) {
                if (Character.isUpperCase(temp)) {
                    int y = (((int) temp - (int) 'A' + key) % 26) + (int) 'A';
                    cypherData.append((char) y);
                } else {
                    int y = (((int) temp - (int) 'a' + key) % 26) + (int) 'a';
                    cypherData.append((char) y);
                }
            } else {
                cypherData.append(temp);
            }
        }
        return cypherData;
    }


    public StringBuilder dec(String data, int key) {
        StringBuilder decryptData = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            char temp = data.charAt(i);
            if (Character.isLetter(temp)) {
                if (Character.isUpperCase(temp)) {
                    int y = ((((int) temp - (int) 'A' - key) + 26) % 26) + (int) 'A';
                    decryptData.append((char) y);
                } else {
                    int y = ((((int) temp - (int) 'a' - key) + 26) % 26) + (int) 'a';
                    decryptData.append((char) y);
                }
            } else {
                decryptData.append(temp);
            }
        }

        return decryptData;
    }

}

class UnicodeTableAlgorithm implements CryptographyAlgorithm {

    public StringBuilder enc(String data, int key) {

        StringBuilder cypherData = new StringBuilder();

        for (int i = 0; i < data.length(); i++) {
            char temp = data.charAt(i);
            int y = ((int) temp + key);
            cypherData.append((char) y);
        }

        return cypherData;
    }

    public StringBuilder dec(String data, int key) {

        StringBuilder decryptData = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            char temp = data.charAt(i);
            int y = ((int) temp - key);
            decryptData.append((char) y);

        }

        return decryptData;
    }

}

public class Main {
    public static void main(String[] args) throws IOException {
        //input Stage
        String mode, data, key, out, in, alg;

        //1.1 Handel args to key value pair
        HashMap<String, String> params = convertToKeyValuePair(args);
        mode = params.get("mode");
        data = params.get("data");
        key = params.get("key");
        in = params.get("in");
        out = params.get("out");
        alg = params.get("alg");

        if (mode == null)
            mode = "enc";
        if (key == null)
            key = "0";

        if (data == null) {
            if (in == null) {
                data = ""; // handle no in or data args
            } else {
                data = readFileAsString(in);
            }
        }
        if (alg == null) {
            alg = "shift";
        }

        // processing Stage
        Cryptography cryptography;
        if (alg.equals("shift")) {
            cryptography = new Cryptography(new ShiftingAlgorithm());
        } else {
            cryptography = new Cryptography(new UnicodeTableAlgorithm());
        }

        StringBuilder result = mode.equals("enc") ? cryptography.encrypt(data, Integer.parseInt(key)) : cryptography.decrypt(data, Integer.parseInt(key));

        //output Stage
        if (out == null) System.out.println(result);
        else writeFileFromString(out, result); //out holds filename


    }

    private static HashMap<String, String> convertToKeyValuePair(String[] args) {

        HashMap<String, String> params = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            String key = args[i].substring(1);
            String value = args[i + 1];
            params.put(key, value);
        }

        return params;
    }

    private static void writeFileFromString(String fileName, StringBuilder data) throws IOException {
        File outFile = new File(fileName);
        FileWriter writer = new FileWriter(outFile);
        writer.write(String.valueOf(data));
        writer.close();
    }

    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
}