import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Admin {
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        int choice;
        boolean lanjut = true;

        do {
            clearScreen();
            System.out.println("Selamat Datang Admin di penjualan Tiket Konser Samarinda\n");
            System.out.println("1. Menu Lihat");
            System.out.println("2. Cari Konser");
            System.out.println("3. Tambah Konser");
            System.out.println("4. Ubah Konser");
            System.out.println("5. Hapus Konser");
            System.out.println("6. Keluar");
            System.out.print("\nPilih menu: ");
            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    showSubMenu(input);
                    break;
                case 2:
                    System.out.println("\n=========\nCARI KONSER\n=========");
                    cariData();
                    break;
                case 3:
                    System.out.println("\n================\nTAMBAH KONSER\n================");
                    tambahData();
                    break;
                case 4:
                    System.out.println("\n==============\nUBAH KONSER\n==============");
                    ubahData();
                    break;
                case 5:
                    System.out.println("\n===============\nHAPUS KONSER\n===============");
                    hapusData();
                    break;
                case 6:
                    System.out.println("Keluar dari Admin Panel.");
                    lanjut = false;
                    break;
                default:
                    System.err.println("Pilihan tidak ditemukan. Silakan coba lagi.");
            }

            if (lanjut) {
                lanjut = getYesorNo("Apakah Anda ingin melanjutkan?");
            }
        } while (lanjut);

        input.close();
    }

    private static void showSubMenu(Scanner scanner) throws IOException {
        System.out.println("\n=================");
        System.out.println("LIHAT SELURUH DATA");
        System.out.println("1. Konser");
        System.out.println("2. Tiket");
        System.out.println("3. Transaksi");
        System.out.println("4. Kembali");
        System.out.print("Pilih: ");
        int sub = scanner.nextInt();
        scanner.nextLine();

        switch (sub) {
            case 1:
                tampilkanKonser();
                break;
            case 2:
                tampilkanTiket();
                break;
            case 3:
                tampilkanTransaksi();
                break;
            case 4:
                // kembali
                break;
            default:
                System.out.println("Pilihan tidak valid!");
        }
    }

    private static void tampilkanKonser() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("database.txt"))) {
            System.out.println("\n| No | Hari/Tanggal | Konser | Lokasi | Tiket Tersedia | Deskripsi |");
            System.out.println("--------------------------------------------------------------------------------");
            String line;
            int no = 1;
            while ((line = br.readLine()) != null) {
                String[] f = line.split(",", 6);
                System.out.printf("| %2d | %-12s | %-10s | %-10s | %-14s | %s\n",
                        no++, f[1], f[2], f[3], f[4], f[5]);
            }
        } catch (FileNotFoundException e) {
            System.err.println("database.txt tidak ditemukan. Silakan tambah konser terlebih dahulu.");
        }
    }

    private static void tampilkanTiket() throws IOException {
        File file = new File("tiket.txt");
        if (!file.exists()) {
            System.err.println("Belum ada tiket yang tersedia.");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            System.out.println("\n| No | Tiket ID | Konser | Harga | Status |");
            System.out.println("----------------------------------------------");
            String line;
            int no = 1;
            while ((line = br.readLine()) != null) {
                String[] f = line.split(",", 4);
                System.out.printf("| %2d | %-8s | %-10s | %-5s | %s\n",
                        no++, f[0], f[1], f[2], f[3]);
            }
        }
    }

    private static void tampilkanTransaksi() throws IOException {
        File file = new File("transaksi.txt");
        if (!file.exists()) {
            System.err.println("Belum ada transaksi yang tercatat.");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            System.out.println("\n| No | ID Transaksi | User | Konser | Jumlah | Total |");
            System.out.println("--------------------------------------------------------");
            String line;
            int no = 1;
            while ((line = br.readLine()) != null) {
                String[] f = line.split(",", 6);
                System.out.printf("| %2d | %-12s | %-6s | %-6s | %-6s | %s\n",
                        no++, f[0], f[1], f[2], f[3], f[4]);
            }
        }
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
        int tiket = 0;
        System.out.println("\n| No |\tHari/Tanggal       |\tKonser        |\tLokasi       |\tTiket Tersedia     |\tDeskripsi         |");
        System.out.println("----------------------------------------------------------------------------------------------------------");

        while(data != null){

            isExist = true;

            for(String keyword:keywords){
                isExist = isExist && data.toLowerCase().contains(keyword.toLowerCase());
            }

            if(isExist){
                nomorData++;
                tiket++;
                StringTokenizer stringToken = new StringTokenizer(data, ",");

                stringToken.nextToken();
                System.out.printf("| %2d ", nomorData);
                System.out.printf("|\t%-20s  ", stringToken.nextToken());
                System.out.printf("|\t%-20s   ", stringToken.nextToken());
                System.out.printf("|\t%-20s   ", stringToken.nextToken());
                System.out.printf("| %2d ", tiket);
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
        String nomor, tanggal, konser, lokasi, tiket, deskripsi;

        System.out.print("masukkan nomor: ");
        nomor = Input.nextLine();
        tanggal = ambiltanggal();
        System.out.print("masukkan konser: ");
        konser = Input.nextLine();
        System.out.print("masukkan lokasi: ");
        lokasi = Input.nextLine();
        System.out.print("masukkan tiket: ");
        tiket = Input.nextLine();
        System.out.print("masukkan deskripsi konser: ");
        deskripsi = Input.nextLine();

        String[] keywords = {nomor + "," + tanggal +"," + konser +"," + lokasi + "," + tiket + "," + deskripsi};
        System.out.println(Arrays.toString(keywords));

        boolean isExist = cekKonser(keywords,false);

        if (!isExist){
            System.out.println(ambilEntryPertanggal(nomor, tanggal));
            String no = nomor.replaceAll("\\s+","");
            System.out.println("\nkonser yang akan anda masukan adalah");
            System.out.println("----------------------------------------");
            System.out.println("nomor       : " + no);
            System.out.println("tanggal     : " + tanggal);
            System.out.println("konser      : " + konser);
            System.out.println("lokasi      : " + lokasi);
            System.out.println("tiket       : " + tiket);
            System.out.println("deskripsi   : " + deskripsi);

            boolean isTambah = getYesorNo("Apakah akan ingin menambah data konser tersebut? ");

            if(isTambah){
                bufferOutput.write(no + "," + tanggal + ","+ konser +"," + lokasi + "," + tiket + "," + deskripsi);
                bufferOutput.newLine();
                bufferOutput.flush();
            }

        } else {
            System.out.println("konser yang anda akan masukan sudah tersedia di data konser:");
            cekKonser(keywords,true);
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

    private static boolean cekKonser(String[] keywords, boolean isDisplay) throws IOException{

        FileReader fileInput = new FileReader("database.txt");
        BufferedReader bufferInput = new BufferedReader(fileInput);

        String data = bufferInput.readLine();
        boolean isExist = false;
        int nomorData = 0;
        int tiket = 0;

        if (isDisplay) {
            System.out.println("\n| No |\tHari/Tanggal       |\tKonser        |\tLokasi       |\tTiket Tersedia     |\tDeskripsi         |");
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
                    tiket++;
                    StringTokenizer stringToken = new StringTokenizer(data, ",");

                    stringToken.nextToken();
                    System.out.printf("| %2d ", nomorData);
                    System.out.printf("|\t%-20s  ", stringToken.nextToken());
                    System.out.printf("|\t%-20s   ", stringToken.nextToken());
                    System.out.printf("|\t%-20s   ", stringToken.nextToken());
                    System.out.printf("| %2d ", tiket);
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

    private static String ambiltanggal() {
        Scanner input = new Scanner(System.in);
        String tanggalInput;
        boolean tanggalValid = false;
        
        do {
            System.out.print("Masukkan hari dan tanggal (Senin, 25/03/2025): ");
            tanggalInput = input.nextLine();
            
            if (tanggalInput.matches("\\w+,\\s\\d{2}/\\d{2}/\\d{4}")) {
                String[] splitTanggal = tanggalInput.split(",\\s");
                String[] splitTgl = splitTanggal[1].split("/");
                int day = Integer.parseInt(splitTgl[0]);
                int month = Integer.parseInt(splitTgl[1]);

                if (month >= 1 && month <= 12 && day >= 1 && day <= 31) {
                    tanggalValid = true;
                } else {
                    System.out.println("Tanggal tidak valid. Mohon masukkan tanggal yang benar.");
                }
            } else {
                System.out.println("Format tanggal yang anda masukkan salah. Harus dalam format: Hari, DD/MM/YYYY");
            }
        } while (!tanggalValid);

        return tanggalInput;
    }

    private static void ubahData() throws IOException {
        File database = new File("database.txt");
        FileReader fileInput = new FileReader(database);
        BufferedReader bufferedInput = new BufferedReader(fileInput);

        File tempDB = new File("tempDB.txt");
        FileWriter fileOutput = new FileWriter(tempDB);
        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

        System.out.println("List Konser");
        tampilkanKonser();

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
                System.out.println("Referensi   : " + st.nextToken());
                System.out.println("nomor       : " + st.nextToken());
                System.out.println("tanggal     : " + st.nextToken());
                System.out.println("konser      : " + st.nextToken());
                System.out.println("lokasi      : " + st.nextToken());
                System.out.println("tiket       : " + st.nextToken());
                System.out.println("deskripsi   : " + st.nextToken());

                String[] fieldData = {"nomor", "tanggal", "lokasi", "konser", "tiket", "deskripsi"};
                String[] tempData = new String[6];

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
                System.out.println("nomor       : " + st.nextToken() + " --> " + tempData[0]);
                System.out.println("tanggal     : " + st.nextToken() + " --> " + tempData[1]);
                System.out.println("konser      : " + st.nextToken() + " --> " + tempData[2]);
                System.out.println("lokasi      : " + st.nextToken() + " --> " + tempData[3]);
                System.out.println("tiket       : " + st.nextToken() + " --> " + tempData[4]);
                System.out.println("deskripsi   : " + st.nextToken() + " --> " + tempData[5]);

                boolean isUpdate = getYesorNo("apakah sudah yakin ingin mengupdate?");

                if (isUpdate){

                    boolean isExist = cekKonser(tempData,false);

                    if(isExist){
                        System.err.println("data konser sudah ada di ubah, proses update dibatalkan, \nsilahkan delete data yang bersangkutan");
                        bufferedOutput.write(data);

                    } else {

                        String nomor = tempData[0];
                        String tanggal = tempData[1];
                        String konser = tempData[2];
                        String lokasi = tempData[3];
                        String tiket = tempData[4];
                        String deskripsi = tempData[5];

                        long nmrEntry = ambilEntryPertanggal(nomor, tanggal) + 1;

                        String Konser = nomor.replaceAll("\\s+","");
                        String no = Konser+"_"+tanggal+"_"+nmrEntry;

                        bufferedOutput.write(no + "," + nomor + ","+ tanggal +"," + konser + ","+ lokasi + "," + "," + tiket + deskripsi);
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
        tampilkanKonser();

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
                System.out.println("konser          : " + st.nextToken());
                System.out.println("lokasi          : " + st.nextToken());
                System.out.println("tiket          : " + st.nextToken());
                System.out.println("deskripsi       : " + st.nextToken());

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
