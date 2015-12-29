package daimaroo.bash;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.w3c.dom.ls.LSInput;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.io.File;
/**
 * Created by Mostafa on 15/12/13.
 */
public class Repository implements java.io.Serializable {

    //Name Repository With it's path in local machine
    private String pathOfDaimarooData="C:\\Daimaroo\\Daimaroo";

    //List of all repository by daimaroo first argument is id of user and second is repository and it's path by that user
    public HashMap<String,File> listOfAllRepositories=null;

    private String repositoryName=null;
    private String tempOfRepository=null;


    //    private String repositoryPath;

    private String ParentDefaultPath=null;
    private HashMap<File,String> FileHashlist=null;
    private String VersionHolderFolderPath=null;

    /**This Folder Hold The Version Of Our Files And When Need To Back To Last Version  Come And Replace
     * All Of This Files To Master Root And Make New Repository And Get It Back
     * By This Method That Has A Parent HassMap And Two Inner HashMap That First Contains
     * HashMap Of Change File And It's MD5 Of This Files And Second HashMap Contains
     * Data And String Of Commit
     */
    private HashMap<HashMap<File,String>,HashMap<Date,String>> VersionsList=null;


    /**Set Owner Of Repository And Repository name And call Make New Repository
     *
     * @param repositoryname
     */
    public Repository(String repositoryname,File thisdir) throws IOException, NoSuchAlgorithmException {
        this.ParentDefaultPath=thisdir.getAbsolutePath();
        File daimaroodata=new File(this.pathOfDaimarooData);
        FileHandler.mkFileWithAbsolutePath(daimaroodata);
        readFromDaimarooData();
        if(thisDirectoryIsRepository(repositoryname,daimaroodata)==true){
            this.repositoryName=repositoryname;
            this.readFromDataObjectFile();
        }
        else{
            System.out.println("This Directory Is Initializing");
            init(thisdir);
            writeToDaimarooData();

        }

//        this.setParentDefaultPath();
//        makeNewRepository(repositoryname);
//        this.mkTempFolderOfChangedFiles();

    }


    public boolean thisDirectoryIsRepository(String repositoryname,File path){
        if(this.listOfAllRepositories!=null){
            if(this.listOfAllRepositories.get(repositoryname).exists()){
                if(this.listOfAllRepositories.get(repositoryname).equals(path)){
                    while(true){
                        System.out.println("Do You Want To Replace This?y/n\n");
                        Scanner answer=new Scanner(System.in);
                        String input=answer.nextLine();
                        if(input.equals("y") || input.equals("Y")){
                            this.listOfAllRepositories.put(repositoryname,path);
                            System.out.println("(Was Destroyed!) The Last Repository By This Name Was Destroyed");
                            return true;
                        }
                        if(input.equals("n") || input.equals("N")){
                            System.out.println("(Not Destroyed!)We Didn't Destroy Your Repository!");
                            return true;
                        }
                    }
                }

            }
        }

        return false;
    }

    /**Initialize This Directory as A repository
     *
     * @param file
     */
    public void init(File file) throws IOException, NoSuchAlgorithmException {
        String Reponame=makeNameForThisRepository(file);
        if(listOfAllRepositories==null){
            readFromDaimarooData();
            if(listOfAllRepositories==null)
                listOfAllRepositories=new HashMap<>();
        }
        if(listOfAllRepositories!=null){
            if(!listOfAllRepositories.isEmpty()){
                if(listOfAllRepositories.get(Reponame).exists()){
                    System.out.println("This Directory Was Initialized!");
                }
            }
            else{
                this.listOfAllRepositories.put(Reponame,file);
                String path=file.getAbsolutePath();
                this.FileHashlist=FileTree.allfilesWithMD5Hash(path);
                for(File f:FileHashlist.keySet()){
                    System.out.print(f.getAbsolutePath());
                    System.out.println(FileHashlist.get(f));
                }
                writeToDataObjectFile();
            }
        }

    }

    /**Get Tha Absolute Path OF A file And Get The Name Of This File
     *
     * @param file
     * @return
     */
    public String makeNameForThisRepository(File file){
        if(file.isDirectory()){
            return file.getName();
        }
        else {
            System.out.println("(Error):This File Is Not A directory!");
        }
        return null;
    }
    /**Get Name Of This Repository
     *
     */
    public static String getNameOfThisRepository(){
        String AbsolutePath=System.getProperty("user.dir");
        File thisfile=new File(AbsolutePath);
        File daimaroo=new File(AbsolutePath+"\\.daimaroo");
        if(daimaroo.exists()){
            return thisfile.getName();
        }
        else{
            System.out.print("This File Is Not A Repository"+"\nPlease Initialize This Repository\nEnter help\n");
            return null;
        }
    }


    /**Get Two HashMap And Compare It's Files and return Changed Files
     * @param NewHashMap
     * @return
     */
    public ArrayList<File> findChangedFiles(HashMap<File,String> NewHashMap){
        HashMap<File,String> OldHashMap=this.FileHashlist;
        ArrayList<File> changed=new ArrayList<File>();
        for(File file1:NewHashMap.keySet()){
            for(File file2: OldHashMap.keySet()){
                if(FileTree.existFileInTheRepo(file1,OldHashMap)) {
                    if (file1.equals(file2)) {
                        if (!OldHashMap.get(file1).equals(OldHashMap.get(file1))) {
                            changed.add(file2);
                        }
                    }
                }
                else{
                    changed.add(file1);
                }
            }
            if(OldHashMap.size()==0 || OldHashMap==null){
                changed.add(file1);
            }

        }
        return changed;
    }


    private void addToOldHashList(File file1) throws IOException, NoSuchAlgorithmException {
        String hashOfthisFile=FileHandler.getHashOfFile(file1,HashType.MD5);
        this.FileHashlist.put(file1,hashOfthisFile);
    }
    /**Convert Comming File Path from Client To Server Wanted Path
     *
     * @param ClientPath
     * @param Serverpath
     * @return
     */
    public String convertFilePathToServerPath(File ClientPath,String Serverpath){
        String cpath=ClientPath.getAbsolutePath();
        String temp=cpath.replace(this.ParentDefaultPath,"");
        cpath=Serverpath+temp;
        return cpath;
    }





    /** Change The Repository Name
     *
     * @param newRepoName
     */
    public void changeReposname(String newRepoName){
        this.repositoryName=newRepoName;
        this.writeToDataObjectFile();
    }

    /**Set Parent Default Path
     *
     */
    private void setParentDefaultPath(){
        String temp=System.getProperty("user.dir");
        this.ParentDefaultPath=temp;
        this.writeToDataObjectFile();
    }


    /**Make New Repository By Comming String
     *
     * @param repositoryname
     */
    private void makeNewRepository(String repositoryname){
        String Path=this.ParentDefaultPath+"\\"+repositoryname;
        File file=new File(Path);
        System.out.println(file.getAbsolutePath());
        if(!file.exists())
            file.mkdirs();
        this.writeToDataObjectFile();

    }

    /**Return The User Admin Of This Repository
     *
     * @return
    //     */

    /**Return Reposirtory Name
     *
     * @return
     */
    public String getRepositoryName(){
        return this.repositoryName;
    }


    /**Return Repository Path
     *
     * @return
     */
    public String getRepositoryPath(){
        return this.ParentDefaultPath+"\\"+this.repositoryName;
    }




    /**This Repository Will destroy Itself! What The Fuz!!!
     *
     * @return
     */
    public boolean removeThisRepository(){
        File file=new File(this.ParentDefaultPath+"\\"+this.repositoryName);
        if(file.exists()){
            FileHandler.deleteDir(file);
            this.writeToDataObjectFile();
            return true;
        }
        System.out.println("Repository Not Exist");

        return false;
    }

    /**Copy All Of This Repository To New Destination
     *
     * @param des
     */
    public void cloneRepository(String des) throws IOException, NoSuchAlgorithmException {
        String temp;
        String temp1;
        String filepath;
        temp=this.ParentDefaultPath;
        File file2;
        if(this.FileHashlist==null) {
            System.out.println("Path:" + temp);
            this.FileHashlist = FileTree.allfilesWithMD5Hash(this.ParentDefaultPath);
        }
        for(File file:this.FileHashlist.keySet()){
            filepath=file.getAbsolutePath();
            System.out.println("temp:"+temp);
            System.out.println("des:"+des);
            System.out.println("FilePath1:"+filepath);
            filepath.replace(temp, des);
            System.out.println("FilePath2:" + filepath);
            System.out.println("temp1:" + temp);
            file2=new File(filepath);
            FileHandler.copyFile(file,file2);
        }
    }


    /**Make Temp Folder By Set String Path On The Temp Of repository
     *----Worked Correctly!
     */
    public void mkTempFolderOfChangedFiles(){
        File folder=new File(this.ParentDefaultPath);
        String tempPath=folder.getAbsolutePath()+"\\temp-"+this.getRepositoryName();
        File tempFolder=new File(tempPath);
        if(!tempFolder.exists())
            FileHandler.makeDir(tempFolder);
        this.writeToDataObjectFile();
    }


    /**Make String Of Temp-Folder Path
     * And Return The Address Of TEmp Folder Path
     *----Worked Correctly
     * @param file
     * @return
     */
    public String makeNewTempFolderPath(File file){
        String path0=file.getAbsolutePath();
        String pathWithoutDefaultpath = path0.replace(this.ParentDefaultPath, "");
        String path1="\\temp-"+this.repositoryName;
        String FinalPath=path0+path1+pathWithoutDefaultpath;
        this.tempOfRepository=FinalPath;
        this.writeToDataObjectFile();
        return FinalPath;
    }
//    public void AddThisChangedToVersionList{
//    }


    /**Create A Data HashMap That Contains All Of Data From This
     * repository
     * @return
     */
    public HashMap<String,Object> converAllOfDataToHashMap(){
        HashMap<String,Object> Data=new HashMap<String,Object>();
        Data.put("tempOfRepository",this.tempOfRepository);
        Data.put("repositoryName",this.repositoryName);
        Data.put("FileHashList",this.FileHashlist);
        Data.put("ParentDefaultPath",this.ParentDefaultPath);
        Data.put("VersionHolderFolderPath",this.VersionHolderFolderPath);
        return Data;
    }


    /**Write All Of Data That Collected By HashMap Data To
     * File Object That Contains This Object
     * If This File Was Created --Nothing would happens
     */
    private void writeToDataObjectFile(){
        File dataObjectFile=new File(this.ParentDefaultPath+"\\"+".daimaroo"+"\\"+"DataObjectFile.txt");
        if(!dataObjectFile.exists()) {
            File parent=dataObjectFile.getParentFile();
            parent.mkdirs();
        }
        HashMap<String,Object> Data=converAllOfDataToHashMap();
        System.out.println(dataObjectFile);
        if(Data.isEmpty()){
            System.out.println("HashMap Is Empty!");
            return;
        }
//        else{
//            for(String st:Data.keySet()){
//                System.out.println("Data::"+st+"-->"+Data.get(st));
//            }
        System.out.println("HashMap Not Empty!");
        FileHandler.writeObjectInFile(Data, dataObjectFile);
    }



    /**Read Wrote Data From Repository Object File
     *
     */
    private void readFromDataObjectFile(){
        File dataObjectFile=new File(this.ParentDefaultPath+"\\"+this.repositoryName+"\\"+".daimaroo"+"\\"+"DataObjectFile.txt");
        HashMap<String,Object> Data= (HashMap<String, Object>) FileHandler.readObjectInFile(dataObjectFile);
        if(Data!=null){
            this.FileHashlist=(HashMap<File, String>) Data.get("FileHashList");
            this.tempOfRepository= (String) Data.get("tempOfRepository");
            this.repositoryName= (String) Data.get("repositoryName");
            this.ParentDefaultPath= (String) Data.get("ParentDefaultPath");
            this.VersionHolderFolderPath= (String) Data.get("VersionHolderFolderPath");
        }
    }


    /**Read Daimaroo List Of Repositories On This Machine
     *From Daimaroo Data File In The C:\Daiamroo\Daimaroo
     */
    private void readFromDaimarooData(){
        File DaimarooDataFile=new File(this.pathOfDaimarooData);
        if(!DaimarooDataFile.exists()){
            System.out.println("Daimaroo Data Not Exist");
            return;
        }
        HashMap<String,File> Data= (HashMap<String, File>) FileHandler.readObjectInFile(DaimarooDataFile);
        this.listOfAllRepositories=Data;
    }


    /**Write list Of Repositories To The Daimaroo
     * Data File at C:\Daimaroo\Daiamroo
      */
    private void writeToDaimarooData(){
        File DaimarooDataFile=new File(this.pathOfDaimarooData);
        FileHandler.mkFileWithAbsolutePath(DaimarooDataFile);
        if(this.listOfAllRepositories==null){
            this.listOfAllRepositories=new HashMap<>();
        }
        if(this.listOfAllRepositories.isEmpty()){
            System.out.println("HashMap Is Empty!");
            return;
        }
        System.out.println("Daimaroo Data HashMap Is Not Empty!");
        FileHandler.writeObjectInFile(this.listOfAllRepositories,DaimarooDataFile);
    }

}
