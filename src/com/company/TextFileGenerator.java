package com.company;
import java.io.*;
interface Generator<T>
{
    boolean hasNext();
    T next();
}
class TextFileGenerator implements Generator<String>{
    private final static String[] strAry;
    private final static String path ="F:\\test.txt";
    private  Integer index = 0;
    static
    {

        try {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(path).getAbsoluteFile()))) {
                StringBuilder sb = new StringBuilder();
                String tempStore;
                while ((tempStore = bufferedReader.readLine()) != null)
                    sb.append(tempStore);
                strAry = sb.toString().split(" ");
            }
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }

    }
    TextFileGenerator(){
        index=0;
    }

    @Override
    public boolean hasNext() {
        return strAry.length > index;
    }

    @Override
    public String next() {
        int i = index;
        index++;
        return strAry[i];
    }
}
