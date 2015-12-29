package daimaroo.bash;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mostafa on 15/12/12.
 */

public class FileTree implements java.io.Serializable{

    public  List<String> AbsolutePathInOfFilesInAFolder;


    /**Just Print Directory Tree And Not Useful!
     *
     * @param folder
     * @return
     */
    public static String printDirectoryTree(File folder) {
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("folder is not a Directory");
        }
        int indent = 0;
        StringBuilder sb = new StringBuilder();
        printDirectoryTree(folder, indent, sb);
        return sb.toString();
    }


    /**Just Print Directory Tree And Absolutely! Is Not  Useful!
     *
     * @param folder
     * @param indent
     * @param sb
     */
    private static void printDirectoryTree(File folder, int indent,
                                           StringBuilder sb) {
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("folder is not a Directory");
        }
        sb.append(getIndentString(indent));
        sb.append("+--");
        sb.append(folder.getName());
        sb.append("/");
        sb.append("\n");
        try{
            for (File file : folder.listFiles()) {
                if (file.isDirectory()) {
                    printDirectoryTree(file, indent + 1, sb);
                } else {
                    printFile(file, indent + 1, sb);
                }
            }
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }


    }


    /**I Don't Know!It's relative To Print Directory Tree
     *
     * @param file
     * @param indent
     * @param sb
     */
    private static void printFile(File file, int indent, StringBuilder sb) {
        sb.append(getIndentString(indent));
        sb.append("+--");
        sb.append(file.getName());
        sb.append("\n");
    }

    /**I Don't Know!It's relative To Print Directory Tree
     *
     * @param indent
     * @return
     */
    private static String getIndentString(int indent) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            sb.append("|  ");
        }
        return sb.toString();
    }


    /**Make Folder Tree List!
     * If You Need More Information Call ME! :)
     * @param masterFolder
     * @return
     */
    public static List<Object> makeFolderTreeList(File masterFolder){
        ArrayList<Object> master;
        if(masterFolder.isDirectory()){
            master=new ArrayList<Object>();
            master.add(masterFolder.getName());      //Alwayse The first name of list is the name of master
            try{
                for(File file: masterFolder.listFiles()){
                    if(file.isDirectory()){
                        ArrayList<Object> innerFolder=new ArrayList<Object>();
                        makeFolderTreeList(file);
                    }
                    if(file.isFile()){   //If file wasn't a directory it's name be added to director
                        master.add(file.getName());
                    }
                }
            }
            catch(NullPointerException e){
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Enter A folder Name Not File name!");
            return null;
        }
        return master;
    }





    /**Search In Lsit Of Tree For Special File and REturn This File
     * Or Folder
     * @param foldertree
     * @param name
     * @return
     */
    public static Object searchInListTree(List<Object> foldertree,File name){
        String fname=name.getName();
        for(Object obj: foldertree){

            if(obj instanceof List){
                if(((List<Object>) obj).get(0).equals(fname)){
                    return obj;
                }
                else{
                    searchInListTree((List<Object>) obj,name);
                }
            }
            if (obj instanceof String){
                if(obj.equals(fname)){
                    return obj;
                }
            }
        }
        return null;
    }



    /**This Function Search For A File In  A Folder
     *
     * @param dir
     * @param filename
     * @return
     */
    public static File searchForFileInTheTree(File dir,String filename){

        if(dir.isDirectory()){
            for(File file : dir.listFiles()){
                if(file.isFile()){
                    if(file.getName().equals(filename)){
                        return file;
                    }
                }
                if(file.isDirectory()){
                    if(file.getName().equals(filename)){
                        return file;
                    }
                    else{
                        searchForFileInTheTree(file,filename);
                    }
                }
            }
        }
        else{
            System.out.println("Please Enter A Folder Name!");
        }
        return null;
    }




    /**This Function Make A HashMap From List Of Absolute Path
     * Of Files By List Of It Files
     * @return
     */
    public static HashMap<String,String> makeFileOfHashFiles(){
        return null;
    }




    /**Get Absolute Address Of Files In A Folder And It's Sub Folders
     *
     * @param directoryName
     */
    public  static void listFilesAndFilesSubDirectories(String directoryName,ArrayList<String> AbsolutePathInOfFilesInAFolder) {

        File directory = new File(directoryName);

        //get all the files from a directory

        if(directory.isDirectory()){
            File[] fList = directory.listFiles();

            for (File file : fList) {

                if (file.isFile()) {

                    AbsolutePathInOfFilesInAFolder.add(file.getAbsolutePath());

                } else if (file.isDirectory()) {

                    listFilesAndFilesSubDirectories(file.getAbsolutePath(),AbsolutePathInOfFilesInAFolder);

                }

            }

        }
        else{
            System.out.println("Input String Is not An Address Of A Directory!");
        }
    }




    /**Get A Directory And Make A HashMap From All Files In Directory And It's
     * Directory And Make An MD5 Hash
     * @param directory
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static HashMap<File,String> allfilesWithMD5Hash(String directory) throws IOException, NoSuchAlgorithmException {
        ArrayList<String> listOfAllFiles=new ArrayList<String>();
        String MD5hash;
        HashMap<File,String> returnHashMap=new HashMap<File,String>();
        File file;
        FileTree.listFilesAndFilesSubDirectories(directory,listOfAllFiles);
        for(String path:listOfAllFiles){
            file=new File(path);
            MD5hash=FileHandler.getHashOfFile(file,HashType.MD5);
            returnHashMap.put(file,MD5hash);
        }
        return returnHashMap;
    }



    /**Search In The Repository List And Return True If This
     * File Exist In The Repository
     * @param file
     * @param repositoryList
     * @return
     */
    public static boolean existFileInTheRepo(File file,HashMap<File,String> repositoryList){
        if(repositoryList.get(file)!=null){
            return true;
        }
        return false;
    }



    /**Get A File And Search In The Repository HasMap And If Md5 Is Equal Return True
     *
     * @param file
     * @param oldHashMap
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static boolean CompareMD5s(File file,HashMap<File,String> oldHashMap) throws IOException, NoSuchAlgorithmException {
        String HashOfoldFile=oldHashMap.get(file);
        String HashOfNewFile=FileHandler.getHashOfFile(file,HashType.MD5);
        if(HashOfoldFile.equals(HashOfNewFile)){
            return true;
        }
        return false;
    }


    /**Return The List Of File Path That Gotten By Function
     * listFilesAndFilesSubDirectories
     * @return
     */
    public List<String> getPathesOfFiles(){
        return this.AbsolutePathInOfFilesInAFolder;
    }

}