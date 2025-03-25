package module.admin;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.StringTokenizer;

public class MenuAdmin {
    public static void main(String[] args) throws IOException {
        Scanner Input = new Scanner(System.in);
        String tanggal;
        String pilihan;
        boolean lanjut = true;
        static List<Konser> daftarKonser = new ArrayList<>();
        static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy", Locale.ENGLISH);

        while (lanjut) {
            clearScreen();
            System.out.println("Selamat Datang Admin di penjualan Tiket Konser Samarinda\n");
            System.out.println("1.\tMenu Lihat");
            System.out.println("2.\tCari konser");
            System.out.println("3.\tTambah konser");
            System.out.println("4.\tUbah konser");
            System.out.println("5.\tHapus konser");

            System.out.print("\n\nPilih menu: ");
            pilihan = Input.next();

            switch (pilihan) {
                case "1":
                    System.out.println("\n=================");
                    System.out.println("LIST SELURUH KONSER");
                    System.out.println("=================");
                    tampilkanData();
                    break;
                case "2":
                    System.out.println("\n=========");
                    System.out.println("CARI KONSER");
                    System.out.println("=========");
                    cariData();
                    break;
                case "3":
                    System.out.println("\n================");
                    System.out.println("TAMBAH KONSER");
                    System.out.println("================");
                    tambahData();
                    break;
                case "4":
                    System.out.println("\n==============");
                    System.out.println("UBAH KONSER");
                    System.out.println("==============");
                    ubahData();
                    break;
                case "5":
                    System.out.println("\n===============");
                    System.out.println("HAPUS KONSER");
                    System.out.println("===============");
                    hapusData();
                    break;
                default:
                    System.err.println("\nInput tidak ditemukan\nSilahkan pilih lagi : ");
            }

            lanjut = getYesorNo("Apakah Anda ingin melanjutkan?");
        }
    }

    private static void tampilkanData() throws IOException{

        FileReader fileInput;
        BufferedReader bufferInput;

        try {
            fileInput = new FileReader("database.txt");
            bufferInput = new BufferedReader(fileInput);
        } catch (Exception e){
            System.err.println("Konser Tidak ditemukan");
            System.err.println("Silahkan tambah konser terlebih dahulu");
            return;
        }


        System.out.println("\n| No |\tHari/Tanggal       |\tKonser                |\tLokasi               |\tDeskripsi               |");
        System.out.println("----------------------------------------------------------------------------------------------------------");

        String data = bufferInput.readLine();
        int nomorData = 0;
        while(data != null) {
            nomorData++;

            StringTokenizer stringToken = new StringTokenizer(data, ",");

            stringToken.nextToken();
            System.out.printf("| %2d ", nomorData);
            System.out.printf("|\t%4s  ", stringToken.nextToken());
            System.out.printf("|\t%-20s   ", stringToken.nextToken());
            System.out.printf("|\t%-20s   ", stringToken.nextToken());
            System.out.printf("|\t%s   ", stringToken.nextToken());
            System.out.print("\n");

            data = bufferInput.readLine();
        }

        System.out.println("----------------------------------------------------------------------------------------------------------");
    }

    private static void cariData() throws IOException{

        try {
            File file = new File("database.txt");
        } catch (Exception e){
            System.err.println("Konser Tidak ditemukan");
            System.err.println("Silahkan tambah konser terlebih dahulu");
            return;
        }

        Scanner Input = new Scanner(System.in);
        System.out.print("Masukan konser yang ingin dicari : ");
        String cariString = Input.nextLine();
        String[] keywords = cariString.split("\\s+");

        cekKonser(keywords);

    }

    private static void cekKonser(String[] keywords) throws IOException{

        FileReader fileInput = new FileReader("database.txt");
        BufferedReader bufferInput = new BufferedReader(fileInput);

        String data = bufferInput.readLine();
        boolean isExist;
        int nomorData = 0;
        System.out.println("\n| No |\tHari/Tanggal       |\tKonser                |\tLokasi               |\tDeskripsi               |");
        System.out.println("----------------------------------------------------------------------------------------------------------");

        while(data != null){

            isExist = true;

            for(String keyword:keywords){
                isExist = isExist && data.toLowerCase().contains(keyword.toLowerCase());
            }

            if(isExist){
                nomorData++;
                StringTokenizer stringToken = new StringTokenizer(data, ",");

                stringToken.nextToken();
                System.out.printf("| %2d ", nomorData);
                System.out.printf("|\t%4s  ", stringToken.nextToken());
                System.out.printf("|\t%-20s   ", stringToken.nextToken());
                System.out.printf("|\t%-20s   ", stringToken.nextToken());
                System.out.printf("|\t%s   ", stringToken.nextToken());
                System.out.print("\n");
            }

            data = bufferInput.readLine();
        }
        System.out.println("----------------------------------------------------------------------------------------------------------");

    }

    private static void tambahData() throws IOException{


        FileWriter fileOutput = new FileWriter("database.txt",true);
        BufferedWriter bufferOutput = new BufferedWriter(fileOutput);

        Scanner Input = new Scanner(System.in);
        String nomor, tanggal, konser, lokasi, deskripsi;

        System.out.print("masukkan nomor: ");
        nomor = Input.nextLine();
        System.out.print("masukkan hari dan tanggal(hari,DD/MM/YYYY): ");
        tanggal = ambiltanggal();
        System.out.print("masukkan konser: ");
        konser = Input.nextLine();
        System.out.print("masukkan lokasi: ");
        lokasi = Input.nextLine();
        System.out.print("masukkan deskripsi konser: ");
        deskripsi = Input.nextLine();

        String[] keywords = {tanggal+","+nomor+","+lokasi+","+konser+","+deskripsi};
        System.out.println(Arrays.toString(keywords));

        boolean isExist = cekBukuDiDatabase(keywords,false);

        if (!isExist){
            System.out.println(ambilEntryPertanggal(nomor, tanggal));
            long nmrEntry = ambilEntryPertanggal(nomor, tanggal) + 1;
            String Konser = nomor.replaceAll("\\s+","");
            String no = Konser+"_"+tanggal+"_"+nmrEntry;
            System.out.println("\nkonser yang akan anda masukan adalah");
            System.out.println("----------------------------------------");
            System.out.println("nomor : " + no);
            System.out.println("tanggal : " + tanggal);
            System.out.println("konser      : " + konser);
            System.out.println("lokasi        : " + lokasi);
            System.out.println("deskripsi     : " + deskripsi);

            boolean isTambah = getYesorNo("Apakah akan ingin menambah data konser tersebut? ");

            if(isTambah){
                bufferOutput.write(no + "," + tanggal + ","+ konser +"," + lokasi + ","+deskripsi);
                bufferOutput.newLine();
                bufferOutput.flush();
            }

        } else {
            System.out.println("konser yang anda akan masukan sudah tersedia di data konser:");
            cekBukuDiDatabase(keywords,true);
        } 
        bufferOutput.close();
    }

    private static long ambilEntryPertanggal(String nomor, String tanggal) throws IOException {
        FileReader fileInput = new FileReader("database.txt");
        BufferedReader bufferInput = new BufferedReader(fileInput);

        long entry = 0;
        String data = bufferInput.readLine();
        Scanner dataScanner;
        String no;

        while(data != null){
            dataScanner = new Scanner(data);
            dataScanner.useDelimiter(",");
            no = dataScanner.next();
            dataScanner = new Scanner(no);
            dataScanner.useDelimiter("_");

            nomor = nomor.replaceAll("\\s+","");

            if (nomor.equalsIgnoreCase(dataScanner.next()) && tanggal.equalsIgnoreCase(dataScanner.next()) ) {
                entry = dataScanner.nextInt();
            }

            data = bufferInput.readLine();
        }

        return entry;
    }

    private static boolean cekBukuDiDatabase(String[] keywords, boolean isDisplay) throws IOException{

        FileReader fileInput = new FileReader("database.txt");
        BufferedReader bufferInput = new BufferedReader(fileInput);

        String data = bufferInput.readLine();
        boolean isExist = false;
        int nomorData = 0;

        if (isDisplay) {
            System.out.println("\n| No |\tHari/Tanggal       |\tKonser                |\tLokasi               |\tDeskripsi               |");
            System.out.println("----------------------------------------------------------------------------------------------------------");
        }

        while(data != null){

            isExist = true;

            for(String keyword:keywords){
                isExist = isExist && data.toLowerCase().contains(keyword.toLowerCase());
            }

            if(isExist){
                if(isDisplay) {
                    nomorData++;
                    StringTokenizer stringToken = new StringTokenizer(data, ",");

                    stringToken.nextToken();
                    System.out.printf("| %2d ", nomorData);
                    System.out.printf("|\t%4s  ", stringToken.nextToken());
                    System.out.printf("|\t%-20s   ", stringToken.nextToken());
                    System.out.printf("|\t%-20s   ", stringToken.nextToken());
                    System.out.printf("|\t%s   ", stringToken.nextToken());
                    System.out.print("\n");
                } else {
                    break;
                }
            }

            data = bufferInput.readLine();
        }

        if (isDisplay){
            System.out.println("----------------------------------------------------------------------------------------------------------");
        }

        return isExist;
    }

    private static String ambiltanggal() throws IOException{
        boolean tanggalValid = false;
        Scanner Input = new Scanner(System.in);
        String tanggalInput = Input.nextLine();
        while(!tanggalValid) {
            try {
                Year.parse(tanggalInput);
                tanggalValid = true;
            } catch (Exception e) {
                System.out.println("Format tanggal yang anda masukan salah,(hari, DD/MM/YYYY)");
                System.out.print("silahkan masukan tanggal lagi: ");
                tanggalValid = false;
                tanggalInput = Input.nextLine();
            }
        }

        return tanggalInput;
    }

    private static void ubahData() throws IOException{
        File database = new File("database.txt");
        FileReader fileInput = new FileReader(database);
        BufferedReader bufferedInput = new BufferedReader(fileInput);

        File tempDB = new File("tempDB.txt");
        FileWriter fileOutput = new FileWriter(tempDB);
        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

        System.out.println("List Konser");
        tampilkanData();

        Scanner Input = new Scanner(System.in);
        System.out.print("\nMasukan konser mana yang akan diupdate: ");
        int updateNum = Input.nextInt();

        String data = bufferedInput.readLine();
        int entryCounts = 0;

        while (data != null){
            entryCounts++;

            StringTokenizer st = new StringTokenizer(data,",");

            if (updateNum == entryCounts){
                System.out.println("\nKonser yang ingin di update adalah:");
                System.out.println("---------------------------------------");
                System.out.println("Referensi           : " + st.nextToken());
                System.out.println("nomor               : " + st.nextToken());
                System.out.println("tanggal             : " + st.nextToken());
                System.out.println("lokasi            : " + st.nextToken());
                System.out.println("konser               : " + st.nextToken());
                System.out.println("deskripsi               : " + st.nextToken());

                String[] fieldData = {"nomor", "tanggal", "lokasi", "konser", "deskripsi"};
                String[] tempData = new String[5];

                st = new StringTokenizer(data,",");
                String originalData = st.nextToken();

                for(int i=0; i < fieldData.length ; i++) {
                    boolean isUpdate = getYesorNo("apakah anda ingin merubah " + fieldData[i]);
                    originalData = st.nextToken();
                    if (isUpdate){

                        if (fieldData[i].equalsIgnoreCase("tanggal")){
                            System.out.print("masukan tanggal(hari, DD/MM/YYYY): ");
                            tempData[i] = ambiltanggal();
                        } else {
                            Input = new Scanner(System.in);
                            System.out.print("\nMasukan " + fieldData[i] + " baru: ");
                            tempData[i] = Input.nextLine();
                        }

                    } else {
                        tempData[i] = originalData;
                    }
                }

                st = new StringTokenizer(data,",");
                st.nextToken();
                System.out.println("\nkonser terbaru adalah ");
                System.out.println("---------------------------------------");
                System.out.println("nomor               : " + st.nextToken() + " --> " + tempData[0]);
                System.out.println("tanggal             : " + st.nextToken() + " --> " + tempData[1]);
                System.out.println("konser           : " + st.nextToken() + " --> " + tempData[2]);
                System.out.println("lokasi               : " + st.nextToken() + " --> " + tempData[3]);
                System.out.println("deskripsi               : " + st.nextToken() + " --> " + tempData[4]);



                boolean isUpdate = getYesorNo("apakah sudah yakin ingin mengupdate?");

                if (isUpdate){

                    boolean isExist = cekBukuDiDatabase(tempData,false);

                    if(isExist){
                        System.err.println("data konser sudah ada di ubah, proses update dibatalkan, \nsilahkan delete data yang bersangkutan");
                        bufferedOutput.write(data);

                    } else {

                        String nomor = tempData[0];
                        String tanggal = tempData[1];
                        String konser = tempData[2];
                        String lokasi = tempData[3];
                        String deskripsi = tempData[4];

                        long nmrEntry = ambilEntryPertanggal(nomor, tanggal) + 1;

                        String Konser = nomor.replaceAll("\\s+","");
                        String no = Konser+"_"+tanggal+"_"+nmrEntry;

                        bufferedOutput.write(no + "," + nomor + ","+ tanggal +"," + konser + ","+ lokasi + "," + deskripsi);
                    }
                } else {
                    bufferedOutput.write(data);
                }
            } else {
                bufferedOutput.write(data);
            }
            bufferedOutput.newLine();

            data = bufferedInput.readLine();
        }

        bufferedOutput.flush();
        database.delete();
        tempDB.renameTo(database);

    }

    private static void hapusData() throws  IOException{
        File database = new File("database.txt");
        FileReader fileInput = new FileReader(database);
        BufferedReader bufferedInput = new BufferedReader(fileInput);

        File tempDB = new File("tempDB.txt");
        FileWriter fileOutput = new FileWriter(tempDB);
        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

        System.out.println("List Konser");
        tampilkanData();

        Scanner Input = new Scanner(System.in);
        System.out.print("\nMasukan konser yang ingin dihapus: ");
        int deleteNum = Input.nextInt();


        boolean isFound = false;
        int entryCounts = 0;

        String data = bufferedInput.readLine();

        while (data != null){
            entryCounts++;
            boolean isDelete = false;

            StringTokenizer st = new StringTokenizer(data,",");

            if (deleteNum == entryCounts){
                System.out.println("\nKonser yang ingin anda hapus adalah:");
                System.out.println("-----------------------------------");
                System.out.println("Referensi       : " + st.nextToken());
                System.out.println("nomor           : " + st.nextToken());
                System.out.println("tanggal         : " + st.nextToken());
                System.out.println("konser        : " + st.nextToken());
                System.out.println("lokasi           : " + st.nextToken());
                System.out.println("deskripsi           : " + st.nextToken());

                isDelete = getYesorNo("Apakah yakin ingin menghapus?");
                isFound = true;
            }

            if(isDelete){
                System.out.println("Data konser berhasil dihapus");
            } else {
                bufferedOutput.write(data);
                bufferedOutput.newLine();
            }
            data = bufferedInput.readLine();
        }

        if(!isFound){
            System.err.println("konser tidak ditemukan");
        }

        bufferedOutput.flush();
        database.delete();
        tempDB.renameTo(database);

    }

    private static boolean getYesorNo(String message){
        Scanner Input = new Scanner(System.in);
        System.out.print("\n"+message+" (y/n)? ");
        String pilihan = Input.next();

        while(!pilihan.equalsIgnoreCase("y") && !pilihan.equalsIgnoreCase("n")) {
            System.err.println("Pilihan anda bukan y atau n?");
            System.out.print("\n"+message+" (y/n)? ");
            pilihan = Input.next();
        }

        return pilihan.equalsIgnoreCase("y");

    }

    private static void clearScreen(){
        try {
            if (System.getProperty("os.name").contains("Windows")){
                new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033\143");
            }
        } catch (Exception ex){
            System.err.println("tidak bisa refresh");
        }
    }
}