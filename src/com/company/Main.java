/*
Copyright 2020 Emmanuel Nwogu

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

*/

/*
 * The aim of this program is to write word palindromes to a text file line-by-line.
 * This program is powered by WordNet® Princeton, a large lexical database of English and JWI, an MIT Java Library for
 * interfacing with Wordnet.
 */

package com.company;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.POS;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class Main {

    public static void main(String[] args) throws IOException {

        // The word palindromes will later be added to this list
        ArrayList<String> palindromes = new ArrayList<>();

        // This loads an Dictionary Object, defined in the JWI library, from the WordNet files
        // in the wordnetdictionary package.
        URL url = Main.class.getResource("wordnetdictionary");
        IDictionary dict = new Dictionary (url);
        dict.open();

        // Here, we iterate through every POS (Part of Speech).
        for (POS pos : POS.values()) {

            /* Here, we create an iterator that will iterate over all index words of the specified part of speech.
             * In the JWI library, the IIndexWord interface describes a structure that will be useful for an English
             * dictionary. However, the IIndexWord's lemma is what is of importance to this program. Lemma is
             * the form of a word that appears as an entry in a dictionary.
             */
            Iterator<IIndexWord> indexWordIterator = dict.getIndexWordIterator(pos);

            while (indexWordIterator.hasNext()) {
                IIndexWord indexWord = indexWordIterator.next();

                //  Here, we get the lemma of the IIndexWord
                String rawWord = indexWord.getLemma();

                /* Here, we convert the string in the JWI's lemma form to a form that could be found
                 * in a conventional dictionary */
                String literalWord = refineWord(rawWord, true);

                /* Here, we convert the string in the JWI's lemma form to a form adequate for palindrome checking.
                 * Said form is a String with no whitespace */
                String wordForCriteria = refineWord(rawWord, false);

                // Checking if the word is a palindrome.
                if (passesAllCriteria(wordForCriteria)) {
                    // Checking if the word is already in the ArrayList - palindromes.
                    if (!palindromes.contains(literalWord)) {
                        //  Adding the appropriate form of the word. One that could be found
                        //  in a conventional dictionary
                        palindromes.add(literalWord);
                    }
                }
            }
        }
        // Sorting the palindromes list alphabetically.
        palindromes.sort(String::compareToIgnoreCase);

        // Creating our custom writer for writing the palindromes to the appropriate file.
        PalindromeWriter writer = new PalindromeWriter(palindromes);
        // writing the palindromes to the appropriate file.
        writer.write();
    }

    /*This method uses its boolean to determine the form of its return String object
    * @param  convertToLiteral  if true, the return String is in a form that could be found in
    *                          a conventional dictionary, else the return String is in a form that is necessary (with no
    *                          whitespace).
    * @param  str   This parameter is intended for the lemma of an IIndexWord object from the JWI library.
    * @return      The string determined by the convertToLiteral parameter.
     * */
    public static String refineWord(String str, boolean convertToLiteral) {
        if (convertToLiteral) {
            return str.replace("_", " ");
        } else {
            return str.replace("_", "");
        }
    }

    /* Returns true if the parameter word is a palindrome and relevant to our program. To be relevant to our program,
    *  the program has to be a non-numeric, non-repeating palindrome longer than two letters.
    *  This method uses other pre-defined methods to perform its task.
    *  @param  word  the word to be checked
    *  */
    public static boolean passesAllCriteria(String word) {
        return !word.contains("_") && isPalindrome(word) && isRelevant(word) && !isRepeating(word);
    }

    /* This method checks if a word is a word palindrome which is a word that is the same as its reversed form. Examples
    *  include "racecar", "deed", etc.
    *  @param   str   The word to be checked.
    *  @Returns    true if the word is a palindrome else, false.
    * */
    public static boolean isPalindrome(String str) {
        str = str.toLowerCase();
        int n = str.length();

        for (int i = 0; i < (n / 2); i++) {
            if (str.charAt(i) != str.charAt(n - i - 1)) {
                return false;
            }
        }
        return true;
    }

    /* This method determines if the palindrome is "relevant". To be "relevant", the word must not be numeric
     * and its length must exceed two.
     * @param    str    The word to be checked.
     * @return          true if the word is relevant, else false.
     * */
    public static boolean isRelevant(String str) {
        if (isNumeric(str)) {
            return false;
        } else return str.length() >= 3;
    }

    /* This method determines if a string is a number (or numeric).
     * @param    str   The word to be checked.
     * @return         true if the word is numeric, else false.
     */
    public static boolean isNumeric(String strNum) {
        // a null string can not be numeric.
        if (strNum == null) {
            return false;
        }
        /* If this try-catch block does not throw an exception, the string was succesfully converted to a double
         * and is definitely numeric. If numeric, true is returned in the catch block else false is returned after the
         * try-catch block.
         */
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /* This method determines if a string is a sequence of repeating characters.
     * @param    str   The word to be checked.
     * @return         true if the word is repeating, else false.
     */
    public static boolean isRepeating(String str) {
        int n = str.length();
        if (n < 2) {
            return false;
        }
        for (int i = 0; i < n - 1; i++) {
            if (str.charAt(i) != str.charAt(i + 1)) {
                return false;
            }
        }
        return true;
    }
}
