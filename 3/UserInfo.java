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
           e.printStackTrace();
       }finally{
           objectInputStream.close();
           fileInputStream.close(); 
       }

       return ll;
   }

   public static List<UserInfo> filter(String fn,int age) throws Exception{
       LinkedList<UserInfo> ll=readAsCollection(fn);
       return ll.stream().filter( (ui) -> ui.getAge()<age ).collect( Collectors.toList() );
   }

   public static void main(String[]s) throws Exception{
       //write objects
       FileOutputStream fileOutputStream = new FileOutputStream("save.ser");
       ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

       UserInfo userInfo = new UserInfo("Ivan", "Ivanov", 36);
       objectOutputStream.writeObject(userInfo);
                userInfo = new UserInfo("Vasya", "Pupkin", 33);
       objectOutputStream.writeObject(userInfo);


       objectOutputStream.close();

       //read objects

       System.out.println( filter("save.ser",34) );

       System.out.println( filter("save.ser",37) );

       System.out.println( filter("save.ser",32) );

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
