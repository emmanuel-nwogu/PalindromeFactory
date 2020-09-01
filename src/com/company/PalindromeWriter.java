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


/* This class writes the palindromes, in the list produced in thhe main program, to the already created text file:
 * palindromes.txt.
 */

package com.company;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class PalindromeWriter {
    // Intended for referring to the palindromes ArrayList initialised and updated in the main program.
    private final ArrayList<String> mPalindromeList;

    // The constructor only takes the palindromes ArrayList, initialised and updated in the main program,
    // as a parameter.
    public PalindromeWriter(ArrayList<String> PalindromeList) {
        mPalindromeList = PalindromeList;
    }

    /* This method perform the class's primary task - writing the palindromes to palindromes.txt.
     */
    public void write() {
        try {
            /*The second argument is set to false to overwrite the text file.
             */
            FileWriter fw = new FileWriter("palindromes.txt", false);

            int size = mPalindromeList.size();

            /* Iterating through the palindromes ArrayList and adding each palindrome to the text file.
             */
            for (int i = 0; i < size; i++) {
                // This if-else block is implemented to avoid adding an empty line to the end of the file.
                if (i != size - 1) {
                    fw.append(mPalindromeList.get(i)).append("\n");
                } else {
                    fw.append(mPalindromeList.get(i));
                }
            }
            fw.flush();
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
