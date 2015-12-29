package daimaroo.bash;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by Mostafa on 15/12/24.
 */
public class Main {
    static String url=null;
    static String portnum=null;
    public static void main(String argv[])  {

        Main.url=argv[0];//first argument must be url of server
        Main.portnum=argv[1]; //second argument must be port for connecting to the server
        File file=new File("D:\\C\\stack_smashing.c");
        JSONObject testConnection=new JSONObject();
        testConnection.put("Is_Alive","");
        try {
            String testConn=JsonSender.sendJsonTo(Main.url+":"+Main.portnum+"/daimaroo/handler",testConnection);
            JSONObject jo=new JSONObject(testConn);
            System.out.println(jo.get("Is_Alive"));
            if(jo.get("Is_Alive").equals("TRUE")){
                Main.doDmCommand();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    public static void mainin(String[] argv) throws IOException{
        String thisdir=System.getProperty("user.dir");
        if(argv.length>0){
            if(argv[0].equals("dm")){
                try {
                    doDmCommand();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("Call daimaroo:<dm>!");
            }
        }
        else{
            System.out.println("Enter Command!");
        }

    }
    private static void doDmCommand() throws IOException, NoSuchAlgorithmException {
        String Thisdir=System.getProperty("user.dir");
        File thisdirfile=new File(Thisdir);
        String input;
        Scanner scanner=new Scanner(System.in);
        System.out.println(",--------------------------------------------------------------,");
        System.out.println("       Please Insert One Of This Commands Because We Are\n" +
                "       Working On "+System.getProperty("user.dir")+"\n       Directory This Keywords Are:\n       <exit> <init> <help> <reponame> <add> <commit> <push> <pull>");
        System.out.println(",--------------------------------------------------------------,");
        System.out.print("daimaroo>>>");
        input=scanner.nextLine();
        while(!input.equals("exit")){

            //Initialize this directory as a repository
            if(input.equals("init")){
                String name=thisdirfile.getName();
                Repository repo=new Repository(name,thisdirfile);
                System.out.print("Daimaroo>>>");
                input=scanner.nextLine();
                continue;
            }

            //Add All Of changed by this directory and add the to the database of repositories
            if (input.equals("add")){

                addChangeFiles();
                System.out.print("Daimaroo>>>");
                input=scanner.nextLine();
                continue;
            }

            //If this directory is a repository give you name of this repository
            if(input.equals("reponame")){
                Repository.getNameOfThisRepository();
                System.out.print("Daimaroo>>>");
                input=scanner.nextLine();
                continue;
            }

            //Commit repository with new changes
            if (input.equals("commit\n")){
                ;

                System.out.print("Daimaroo>>>");
                input=scanner.nextLine();
                continue;
            }

            //Push this repository to the server
            if (input.equals("push\n")){
//                pushThisDirectory()
                ;

                System.out.print("Daimaroo>>>");
                input=scanner.nextLine();
                continue;
            }

            //Pull Al Of A repository from server to this directory by given name
            if (input.equals("pull\n")){
//                pullThisDirectory();
                ;

                System.out.print("Daimaroo>>>");
                input=scanner.nextLine();
                continue;
            }
            //Give Your Local Dir
            if (input.equals("dir")){
                System.out.println(System.getProperty("user.dir"));
                ;

                System.out.print("Daimaroo>>>");
                input=scanner.nextLine();
                continue;
            }
            //Ignore If entered Enter Key
            if(input.equals("")){
                ;
                System.out.print("Daimaroo>>>");
                input=scanner.nextLine();
                continue;
            }

            //Lock A file with it's absolute path
            if(input.equals("Lock")){
                ;
                System.out.print("Daimaroo>>>");
                input=scanner.nextLine();
                continue;
            }

            //Help of daimaroo bash
            if(input.equals("help")){
                System.out.println("\n\n\n   /---------------------------------------------------------------\\");
                System.out.println("   |<exit>     : for exit daimaroo                                 |");
                System.out.println("   |<reponame> : name of this repository                           |");
                System.out.println("   |<dir>      : absolute path of this directory                   |");
                System.out.println("   |<add>      : add all changed files to repository               |");
                System.out.println("   |<init>     : initialize this directory as repository           |");
                System.out.println("   |<add>      : add all changed files to repository               |");
                System.out.println("   |<push>     : push this repository to the server                |");
                System.out.println("   |<pull>     : pull a repository from server to this directory   |");
                System.out.println("   |<lock>     : lock a file for you and want a path of file       |");
                System.out.println("   |<help>     : help of daimaroo                                  |");
                System.out.println("   \\---------------------------------------------------------------/\n" +
                        "\n");
                System.out.println("   |(Warning):If A Directory Was Not As Daimaroo Repository You    |\n"+
                        "   | Must Initialize It At First                                   |\n");
                System.out.print("Daimaroo>>>");
                input=scanner.nextLine();
                continue;
            }

            //Else say that your command was not recognized
            else {
                System.out.println("Not Recognize This Command!\nEnter help ");

                System.out.print("Daimaroo>>>");
                input=scanner.nextLine();
                continue;
            }
        }
        return;
    }
    public static void addChangeFiles(){
        String Thisdir=System.getProperty("user.dir");

        ;
    }
}
