package com.transpose;


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TransposeTest {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java TransposeTest <input.json> <semitones>");
            return;
        }

        String inputFile = args[0];
        int semitones = Integer.parseInt(args[1]);

        try {
            List<List<Integer>> inputNotes = readFromFile(inputFile);
            List<List<Integer>> transposedTest = transposeTest(inputNotes, semitones);
            writeToFile(transposedTest, "output.json");
            System.out.println("Transposition completed successfully. Output written in output.json");
        } catch (IOException e) {
            System.out.println("Error reading/writing files: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static List<List<Integer>> readFromFile(String filename) throws IOException {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<ArrayList<Integer>>>() {}.getType();
        return gson.fromJson(new FileReader(filename), listType);
    }

    private static void writeToFile(List<List<Integer>> notes, String filename) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(notes);
        FileWriter writer = new FileWriter(filename);
        writer.write(json);
        writer.close();
    }

    private static List<List<Integer>> transposeTest(List<List<Integer>> notes, int semitones) {
        List<List<Integer>> transposedTest = new ArrayList<>();
        for (List<Integer> note : notes) {
            int octave = note.get(0);
            int noteNumber = note.get(1);

           
            noteNumber -= semitones;
            while (noteNumber < 1) {
                noteNumber += 12;
                octave--;
            }
            while (noteNumber > 12) {
                noteNumber -= 12;
                octave++;
            }

            if (octave >= -3 && octave <= 5 && noteNumber >= 1 && noteNumber <= 12) {
            	transposedTest.add(List.of(octave, noteNumber));
            } else {
                throw new IllegalArgumentException("Error: Transposed test falls out of keyboard range.");
            }
        }
        return transposedTest;
    }
}
