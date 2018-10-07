/**�G�Ϥ�/104213012/��ޤT**/
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
                System.out.print("�п�J�nŪ��training_set����ɦW:");
                String filename = keyin.next();
                System.out.print("�п�J�n�D���̰��N��(max_iter):");
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
        public static int Pla(String training_set, int max_iter, String training_iter) throws IOException {//�ɦW,�̤j�N��,
                 /*Ūtraing_set�ɮ�*/
                FileReader fr = new FileReader(""+training_set);
                BufferedReader reader = new BufferedReader(fr);

                /*���G��X�ɮ�*/
                File f = new File(""+training_iter);//�إ߿�X���G�ɮ�
                FileWriter fw = new FileWriter(training_iter);
                BufferedWriter writer = new BufferedWriter(fw);

                /*�string_set���*/
                String s;
                Integer dataId = 0;//tring_set����Iid
                Integer dataNum = 0;//�⦳�X����ƿ�J
                s = reader.readLine();
                HashMap<Integer, float[]> dataMap = new HashMap<Integer, float[]>();//�straining_set

                String[] arys = s.split(" ");//�N�r����s���B�I�ư}�C {y x1 x2...}
                float[] point = new float[arys.length];
                for(int i =0;i<arys.length;i++){
                        point[i] = Float.parseFloat(arys[i]);
                }
                /*�g�J��X��*/
                writer.write("Input:\n");
                writer.newLine();
                writer.write("    (y"+dataId+", X"+dataId+") : " +s);
                writer.newLine();

                
                dataMap.put(dataId,point);//�Ĥ@����Ʃ�Jmap
                int dim = arys.length-1;//��ƪ�����
                while( ( s = reader.readLine() )!= null )
                {       
                        dataNum++;
                        arys = s.split(" ");
                        /*�ˬd����I��ƬO�_�X�z,�L�~�N�s*/
                        if((arys.length-1 != dim)|| (!arys[0].equals("+1") && !arys[0].equals("-1"))){
                                System.out.println("��"+(dataNum)+"����Ʀ��~:"+s);
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

                /*�p��L�{*/
                /*�g�J��X��*/
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
                /*���N�B��*/
                while (iter<=max_iter+1){                   
                        iter++;
                        int errNum = 0;
                        /*�N�C����Ʊa�Jweight��O�_���T*/
                        for (int i = 0;i<=dataId;i++){
                                int sign;
                                float compute = 0;
                                for (int j = 1;j<=dim;j++){//���L��0���s���Oy��
                                        compute += oldWeight[j-1]*dataMap.get(i)[j];//w1*x1 +w2*x2...
                                }
                                compute+= oldWeight[dim];//+w0*1
                                //System.out.println(compute);

                                /*�������t*/
                                if(compute>0) {
                                        sign = +1;
                                }
                                else {
                                        sign = -1;
                                }

                                /*�����~�o��*/
                                if(compute==0 || sign != dataMap.get(i)[0]){
                                        errNum++;
                                        /*�@�o�{�Ĥ@�ӿ��~�N�����weight*/
                                        if (errNum == 1){
                                                for(int j =0;j<dim;j++){
                                                        newWeight[j] += dataMap.get(i)[0]*dataMap.get(i)[j+1];
                                                        
                                                }
                                                newWeight[dim] += dataMap.get(i)[0]*1;
                                                /*�g�J��X��*/
                                                writer.write(iter+"        X"+i+"                     "+newWeight[0]);
                                                for(int j =1;j<=dim;j++){
                                                        writer.write(","+newWeight[j]+"");
                                                }
                                        
                                        }
                                }
                        }
                        /*���S�����~�F*/
                        if (errNum == 0 ){ 
                                /*�g�J��X��*/
                                writer.write(iter+"        none                     "+newWeight[0]);
                                for(int j =1;j<=dim;j++){
                                        writer.write(","+newWeight[j]);
                                }
                                writer.write("           "+errNum+"/"+(dataId+1));
                                writer.newLine();
                                iter = max_iter+2;//�����j��
                        } 
                        else{   /*���S�����G*/
                                writer.write("           "+errNum+"/"+(dataId+1));
                                writer.newLine();
                                if (iter == max_iter+1){
                                        writer.close();
                                        return 2;
                                }
                        }
                        for(int i = 0;i<dim+1;i++){//�NOLD��w update                        
                                oldWeight[i] = newWeight[i]  ;
                        }       

                }
                
                writer.close();
                return 0;
        }
}