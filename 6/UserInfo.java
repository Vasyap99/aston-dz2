import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.LinkedList;
import java.util.List;

import java.util.stream.Collectors;
import java.util.Arrays;

//import java.io.RandomAccessFile;
import java.io.FileWriter;
import java.nio.charset.Charset;


public class UserInfo implements Externalizable {

   private String firstName;
   private String lastName;
   private int age;


   public String toString() {
       return String.format("<User: %s %s %d>",getFirstName(), getLastName(), getAge());
   }

   public UserInfo() {
   }

   public UserInfo(String firstName, String lastName, int age) {
       this.firstName = firstName;
       this.lastName = lastName;
       this.age = age;
   }

   @Override
   public void writeExternal(ObjectOutput out) throws IOException {
       //out.writeObject(this.getFirstName());
       //out.writeObject(this.getLastName());
       //out.writeObject(this.getAge());
       out.writeBytes(String.format("%s:%s\r\n","firstName",getFirstName()));
       out.writeBytes(String.format("%s:%s\r\n","lastName",getLastName()));
       out.writeBytes(String.format("%s:%d\r\n","age",getAge()));
   }

   @Override
   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
       firstName = in.readLine().split("[:]")[1];
       lastName =  in.readLine().split("[:]")[1];
       age = Integer.parseInt(in.readLine().split("[:]")[1]);
   }


   public String getFirstName() {
       return firstName;
   }

   public String getLastName() {
       return lastName;
   }

   public int getAge() {
       return age;
   }

   public static LinkedList<UserInfo> readAsCollection(String fn) throws Exception{
       FileInputStream fileInputStream = null;
       ObjectInputStream objectInputStream = null;

       LinkedList<UserInfo> ll=new LinkedList<>();

       try{
           fileInputStream = new FileInputStream(fn);
           objectInputStream = new ObjectInputStream(fileInputStream);
           
           UserInfo userInfo=null;
           while(true){
                   userInfo = (UserInfo) objectInputStream.readObject();
                   ll.add(userInfo);
           }
       }catch(Exception e){
           //e.printStackTrace();
       }finally{
           objectInputStream.close();
           fileInputStream.close(); 
       }

       return ll;
   }

   static class Cnt{
       int cnt=0;
   }

   public static List<UserInfo> filter(String fn,int age) throws Exception{
       LinkedList<UserInfo> ll=readAsCollection(fn);
       final Cnt cnt=new Cnt();
       List<UserInfo> l1=ll.stream().filter( (ui) -> ui.getAge()<age ).peek( (x)->cnt.cnt++ ).collect( Collectors.toList() );
       try(FileWriter fr=new FileWriter("log.txt",Charset.forName("UTF-8"),true)){
           //raf.writeUTF(String.format("По итогам фильтрации на %s обнаружено %d элементов\r\n","*текущие дату и время*",cnt.cnt));
           fr.write(String.format("По итогам фильтрации на %s обнаружено %d элементов\r\n","*текущие дату и время*",cnt.cnt));
       }
       return l1;
   }

   public static void main(String[]s) throws Exception{
       System.out.println( Arrays.toString(s) );

       if(s.length<1){
           System.out.println("Usage: UserInfo <fileName>");
           return; 
       }

       String fn=s[0];

       //write objects
       FileOutputStream fileOutputStream = new FileOutputStream(fn);
       ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

       UserInfo userInfo = new UserInfo("Ivan", "Ivanov", 36);
       objectOutputStream.writeObject(userInfo);
                userInfo = new UserInfo("Vasya", "Pupkin", 33);
       objectOutputStream.writeObject(userInfo);

       objectOutputStream.writeObject(new UserInfo("Egor", "Petrov", 31));


       objectOutputStream.close();

       //read objects

       System.out.println( filter(fn,34) );

       System.out.println( filter(fn,37) );

       System.out.println( filter(fn,32) );

       System.out.println( filter(fn,30) );

       /*
       FileInputStream fileInputStream = new FileInputStream("save.ser");
       ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);


       UserInfo userInfo1 = (UserInfo) objectInputStream.readObject();
       System.out.println(userInfo1);

                userInfo1 = (UserInfo) objectInputStream.readObject();
       System.out.println(userInfo1);

       objectInputStream.close();*/

   }
}
