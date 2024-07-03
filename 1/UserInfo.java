import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
       out.writeObject(this.getFirstName());
       out.writeObject(this.getLastName());
       out.writeObject(this.getAge());
   }

   @Override
   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
       firstName = (String) in.readObject();
       lastName = (String) in.readObject();
       age = (Integer) in.readObject();
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

   public static void main(String[]s) throws Exception{
       //write object
       FileOutputStream fileOutputStream = new FileOutputStream("save.ser");
       ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

       UserInfo userInfo = new UserInfo("Ivan", "Ivanov", 36);
       objectOutputStream.writeObject(userInfo);
                userInfo = new UserInfo("Vasya", "Pupkin", 33);
       objectOutputStream.writeObject(userInfo);


       objectOutputStream.close();

       //read object
       FileInputStream fileInputStream = new FileInputStream("save.ser");
       ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);


       UserInfo userInfo1 = (UserInfo) objectInputStream.readObject();
       System.out.println(userInfo1);

                userInfo1 = (UserInfo) objectInputStream.readObject();
       System.out.println(userInfo1);

       objectInputStream.close();

   }
}