# Huffman GUI decompressor 
This Java application that provides a user-friendly graphical interface to decompress files compressed using the Huffman compression algorithm. The program leverages Huffman compressor, written in C, to efficiently decompress compressed files while ensuring data integrity and accuracy.

# GUI
The application includes a button for file selection from the menu and a text field for manually entering the file path. Below, there are two buttons. The first one initiates the compression of the selected file, while the second one initiates its decompression.

Right below the buttons, there is a text field that displays the content of the chosen file. If a compressed file (cps) is selected, only its decrypted dictionary is shown. Each subsequent line displays an ASCII character along with its binary code. However, if a file of a different type is selected, its entire content is displayed, and scrolling is possible.

At the bottom, there is user information about the loaded file.

Additionally, the application compares the file size before and after compression, displaying the reduction in size achieved through the compression process. This information allows users to assess the effectiveness of the Huffman compression algorithm in reducing file sizes.

File content to compress:  
<img width="513" alt="Screenshot 2023-05-22 181754" src="https://github.com/jmanko1/Decompressor/assets/120181288/53747dad-73d1-43ed-803e-814da21ac86c">  

Sign codes window:  
<img width="511" alt="Screenshot 2023-05-17 220725xx" src="https://github.com/jmanko1/Decompressor/assets/120181288/233a307b-86d9-44e5-9f87-2df1d02147db">  

# Code structure
The program utilizes an external Huffman compressor written in C, which is integrated into the Java application using ExeRunner. The compressor efficiently compresses the selected file, while the decompressor leverages ExeRunner to initiate the desired file's compression. This seamless integration of the C-based compressor ensures effective and reliable compression and decompression processes, enhancing the overall performance and functionality of the application.

<img width="467" alt="Screenshot 2023-07-28 020313" src="https://github.com/jmanko1/Decompressor/assets/120181288/405e4d9d-8678-40b1-ab86-024276305b2e">
