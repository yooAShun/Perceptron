/**鄭舜文/104213012/資管三**/
import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
class Perceptron{
        public static void main (String args[]) throws IOException{
                Scanner keyin = new Scanner(System.in);
                System.out.print("請輸入要讀的training_set資料檔名:");
                String filename = keyin.next();
                System.out.print("請輸入要求的最高代數(max_iter):");
                int max_iter = keyin.nextInt();
                int result = Pla(filename,max_iter,filename+"_dataresult.txt");
                if(result == 0){
                     System.out.print("PLA converge susccessfully within max_iter");   
                }
                if(result == 1){
                    System.out.print("incorrect input format");   
                }
                if(result == 2){
                    System.out.print("not converged");   
                }
                
        }
        public static int Pla(String training_set, int max_iter, String training_iter) throws IOException {//檔名,最大代數,
                 /*讀traing_set檔案*/
                FileReader fr = new FileReader(""+training_set);
                BufferedReader reader = new BufferedReader(fr);

                /*結果輸出檔案*/
                File f = new File(""+training_iter);//建立輸出結果檔案
                FileWriter fw = new FileWriter(training_iter);
                BufferedWriter writer = new BufferedWriter(fw);

                /*存tring_set資料*/
                String s;
                Integer dataId = 0;//tring_set資料點id
                Integer dataNum = 0;//算有幾筆資料輸入
                s = reader.readLine();
                HashMap<Integer, float[]> dataMap = new HashMap<Integer, float[]>();//存training_set

                String[] arys = s.split(" ");//將字串轉存為浮點數陣列 {y x1 x2...}
                float[] point = new float[arys.length];
                for(int i =0;i<arys.length;i++){
                        point[i] = Float.parseFloat(arys[i]);
                }
                /*寫入輸出檔*/
                writer.write("Input:\n");
                writer.newLine();
                writer.write("    (y"+dataId+", X"+dataId+") : " +s);
                writer.newLine();

                
                dataMap.put(dataId,point);//第一筆資料放入map
                int dim = arys.length-1;//資料的維度
                while( ( s = reader.readLine() )!= null )
                {       
                        dataNum++;
                        arys = s.split(" ");
                        /*檢查資料點資料是否合理,無誤就存*/
                        if((arys.length-1 != dim)|| (!arys[0].equals("+1") && !arys[0].equals("-1"))){
                                System.out.println("第"+(dataNum)+"筆資料有誤:"+s);
                                writer.close();
                                return 1;
                        }
                        else{
                                dataId++;
                                float[] point1 = new float[arys.length];
                                for(int i =0;i<arys.length;i++){
                                        
                                        point1[i] = Float.parseFloat(arys[i]);
                                }                            
                                dataMap.put(dataId,point1);
                                writer.write("    (y"+dataId+", X"+dataId+") : " +s);
                                writer.newLine();
                        }
                }
                fr.close();

                /*計算過程*/
                /*寫入輸出檔*/
                writer.newLine();
                writer.write("Iteration:");
                writer.newLine();
                writer.newLine();
                writer.write("iter_id misclassified_point     ");
                for(int i = 1;i<dim+1;i++){//w1, w2 ,w0....
                        writer.write("w"+i+",");
                }
                writer.write("w0");
                
                writer.write("        error_rate");
                writer.newLine();
                int iter = 0;
                float[] newWeight =new float[dim+1];
                float[] oldWeight =new float[dim+1];
                for(int i = 0;i<dim+1;i++){//w1, w2 ,w0....
                        newWeight[i] = 0;
                        oldWeight[i] = 0;
                }
                writer.write("0        ---                     0");
                for(int i = 1;i<dim+1;i++){//w1, w2 ,w0....
                        writer.write(",0");
                }
                writer.write("           ---");
                writer.newLine();
                /*迭代運算*/
                while (iter<=max_iter+1){                   
                        iter++;
                        int errNum = 0;
                        /*將每筆資料帶入weight算是否正確*/
                        for (int i = 0;i<=dataId;i++){
                                int sign;
                                float compute = 0;
                                for (int j = 1;j<=dim;j++){//跳過第0筆存的是y值
                                        compute += oldWeight[j-1]*dataMap.get(i)[j];//w1*x1 +w2*x2...
                                }
                                compute+= oldWeight[dim];//+w0*1
                                //System.out.println(compute);

                                /*分類正負*/
                                if(compute>0) {
                                        sign = +1;
                                }
                                else {
                                        sign = -1;
                                }

                                /*有錯誤發生*/
                                if(compute==0 || sign != dataMap.get(i)[0]){
                                        errNum++;
                                        /*一發現第一個錯誤就先更改weight*/
                                        if (errNum == 1){
                                                for(int j =0;j<dim;j++){
                                                        newWeight[j] += dataMap.get(i)[0]*dataMap.get(i)[j+1];
                                                        
                                                }
                                                newWeight[dim] += dataMap.get(i)[0]*1;
                                                /*寫入輸出檔*/
                                                writer.write(iter+"        X"+i+"                     "+newWeight[0]);
                                                for(int j =1;j<=dim;j++){
                                                        writer.write(","+newWeight[j]+"");
                                                }
                                        
                                        }
                                }
                        }
                        /*都沒有錯誤了*/
                        if (errNum == 0 ){ 
                                /*寫入輸出檔*/
                                writer.write(iter+"        none                     "+newWeight[0]);
                                for(int j =1;j<=dim;j++){
                                        writer.write(","+newWeight[j]);
                                }
                                writer.write("           "+errNum+"/"+(dataId+1));
                                writer.newLine();
                                iter = max_iter+2;//結束迴圈
                        } 
                        else{   /*仍沒有結果*/
                                writer.write("           "+errNum+"/"+(dataId+1));
                                writer.newLine();
                                if (iter == max_iter+1){
                                        writer.close();
                                        return 2;
                                }
                        }
                        for(int i = 0;i<dim+1;i++){//將OLD的w update                        
                                oldWeight[i] = newWeight[i]  ;
                        }       

                }
                
                writer.close();
                return 0;
        }
}