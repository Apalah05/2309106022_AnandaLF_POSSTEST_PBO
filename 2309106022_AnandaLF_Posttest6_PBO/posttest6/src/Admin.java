import java.io.*;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

interface ErrorHandling {
    void handlingError(Exception e);
    void handlingError(String pesan);
}

class ErrorUtil implements ErrorHandling {
    public static final int MAX_ERRORS = 3;
    private static int hitungerror = 0;

    @Override
    public void handlingError(Exception e) {
        hitungerror++;
        System.err.println("[ERROR] " + e.getMessage());
        if (hitungerror >= MAX_ERRORS) {
            System.err.println("Anda sudah melampaui error yang ditetapkan. Program terhenti.");
            System.exit(1);
        }
    }

    @Override
    public void handlingError(String pesan) {
        hitungerror++;
        System.err.println("[ERROR] " + pesan);
        if (hitungerror >= MAX_ERRORS) {
            System.err.println("Anda sudah melampaui error yang ditetapkan. Program terhenti.");
            System.exit(1);
        }
    }
}

public class Admin {
    private static final String APP_NAME = "Tiket Konser Samarinda - Admin";
    private static final ErrorUtil errorUtil = new ErrorUtil();
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        int choice;
        boolean lanjut = true;

        do {
            clearScreen();
            System.out.println("Selamat Datang Admin di penjualan Tiket Konser Samarinda");
            System.out.println("==================== " + APP_NAME + " ====================");

            System.out.println("1. Menu Lihat");
            System.out.println("2. Cari Konser");
            System.out.println("3. Tambah Konser");
            System.out.println("4. Ubah Konser");
            System.out.println("5. Hapus Konser");
            System.out.println("6. Keluar");

            try {
                System.out.print("\nPilih menu: ");
                choice = input.nextInt();
                input.nextLine();
            } catch (InputMismatchException ime) {
                errorUtil.handlingError("Input invalid, masukkan kembali dari angka dalam menu.");
                input.nextLine();
                continue;
            }
            
            switch (choice) {
                case 1:
                    try { showSubMenu(input); } catch (Exception e) { errorUtil.handlingError(e); }
                    break;
                case 2:
                    System.out.println("================== " + "CARI KONSER" + " ==================");
                    try { cariData(); } catch (Exception e) { errorUtil.handlingError(e); }
                    break;
                case 3:
                    System.out.println("================ " + "TAMBAH KONSER" + " ================");
                    try { tambahData(); } catch (Exception e) { errorUtil.handlingError(e); }
                    break;
                case 4:
                    System.out.println("================ " + "UBAH KONSER" + " ================");
                    try { ubahData(); } catch (Exception e) { errorUtil.handlingError(e); }
                    break;
                case 5:
                    System.out.println("=============== " + "HAPUS KONSER" + " ===============");
                    try { hapusData(); } catch (Exception e) { errorUtil.handlingError(e); }
                    break;
                case 6:
                    System.out.println("Keluar dari Admin Panel.");
                    lanjut = false;
                    break;
                default:
                    errorUtil.handlingError("Pilihan tidak ditemukan. Silakan coba lagi.");
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
                break;
            default:
                errorUtil.handlingError("Pilihan tidak valid!");
        }
    }

    private static void tampilkanKonser() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("database.txt"))) {
            System.out.println("\n| No |       Hari/Tanggal       |         Konser         |      Lokasi      | Tiket Tersedia, Deskripsi |");
            System.out.println("--------------------------------------------------------------------------------");
            String line;
            int no = 1;
            while ((line = br.readLine()) != null) {
                String[] f = line.split(",", 6);
                System.out.printf("| %2d | %-12s/%-10s | %-15s | %-14s | %s |\n", no++, f[1], f[2], f[3], f[4], f[5]);
            }
        } catch (FileNotFoundException e) {
            errorUtil.handlingError("database.txt tidak ditemukan. Silakan tambah konser terlebih dahulu.");
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
            errorUtil.handlingError("Konser Tidak ditemukan");
            errorUtil.handlingError("Silahkan tambah konser terlebih dahulu");
            return;
        }

        Scanner Input = new Scanner(System.in);
        System.out.print("Masukan konser yang ingin dicari : ");
        String cariString = Input.nextLine();
        String[] keywords = cariString.split("\\s+");

        cekKonser(keywords);
    }

    private static void cekKonser(String[] keywords) throws IOException{
        BufferedReader bufferInput = null;

        try {
            FileReader fileInput = new FileReader("database.txt");
            bufferInput = new BufferedReader(fileInput);
        } catch (IOException e) {
            errorUtil.handlingError(e);
            return;
        }

        String data = bufferInput.readLine();
        boolean isExist;
        int nomorData = 0;
        int tiket = 0;
        System.out.println("\n| No        |\tTanggal       |\tKonser        |\tTiket Tersedia     |\tDeskripsi         |\tLokasi     |");
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

        }
        System.out.println("----------------------------------------------------------------------------------------------------------");

    }

    private static void tambahData() throws IOException {
        Scanner input = new Scanner(System.in);
        String nomor, tanggal, konser, lokasi, tiket, harga, deskripsi;
    
        System.out.print("Masukkan nomor konser: ");
        nomor = input.nextLine();
    
        tanggal = ambiltanggal();
    
        System.out.print("Masukkan nama konser: ");
        konser = input.nextLine();
    
        System.out.print("Masukkan lokasi konser: ");
        lokasi = input.nextLine();
    
        System.out.print("Masukkan jumlah tiket tersedia: ");
        tiket = input.nextLine();
    
        System.out.print("Masukkan harga tiket: ");
        harga = input.nextLine();
    
        System.out.print("Masukkan deskripsi konser: ");
        deskripsi = input.nextLine();
    
        String dataBaru = nomor + "," + tanggal + "," + konser + "," + lokasi + "," + tiket + "," + deskripsi;
        boolean isExist = false;
    
        try (BufferedReader reader = new BufferedReader(new FileReader("database.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equalsIgnoreCase(dataBaru)) {
                    isExist = true;
                    break;
                }
            }
        } catch (FileNotFoundException e) { }
    
        if (!isExist) {
            System.out.println("\nKonser yang akan Anda masukkan adalah:");
            System.out.println("----------------------------------------");
            System.out.println("Nomor     : " + nomor);
            System.out.println("Tanggal   : " + tanggal);
            System.out.println("Konser    : " + konser);
            System.out.println("Lokasi    : " + lokasi);
            System.out.println("Tiket     : " + tiket);
            System.out.println("Harga     : " + harga);
            System.out.println("Deskripsi : " + deskripsi);
    
            boolean isTambah = getYesorNo("Apakah Anda ingin menambah data konser ini?");
    
            if (isTambah) {
                // Simpan ke database.txt
                try (BufferedWriter bufferOutput = new BufferedWriter(new FileWriter("database.txt", true))) {
                    bufferOutput.write(dataBaru);
                    bufferOutput.newLine();
                    bufferOutput.flush();
                }
    
                // Simpan tiket ke tiket.txt
                int jumlahTiket = Integer.parseInt(tiket);
                try (BufferedWriter tiketWriter = new BufferedWriter(new FileWriter("tiket.txt", true))) {
                    for (int i = 1; i <= jumlahTiket; i++) {
                        String idTiket = nomor.replaceAll("\\s+", "") + "_" + tanggal.replaceAll("\\s+", "") + "_T" + i;
                        tiketWriter.write(idTiket + "," + konser + "," + harga + ",tersedia");
                        tiketWriter.newLine();
                    }
                    System.out.println("Tiket berhasil ditambahkan ke tiket.txt sebanyak " + jumlahTiket);
                } catch (IOException e) {
                    errorUtil.handlingError(e);
                }
            }
    
        } else {
            System.out.println("Konser yang Anda ingin tambahkan sudah ada dalam data.");
            String[] keywords = {nomor, tanggal, konser, lokasi, tiket, deskripsi};
            cekKonser(keywords, true);
        }
    }    

    private static long ambilEntryPertanggal(String nomor, String tanggal) throws IOException {
        BufferedReader bufferInput = null;

        try {
            FileReader fileInput = new FileReader("database.txt");
            bufferInput = new BufferedReader(fileInput);
        } catch (IOException e) {
            errorUtil.handlingError(e);
            return 0;
        }

        long entry = 0;
        String data = bufferInput.readLine();
        String nomorBersih = nomor.replaceAll("\\s+", "");

        while(data != null){
            Scanner dataScanner = new Scanner(data);
            dataScanner.useDelimiter("_");

            if (dataScanner.hasNext()) {
                String no = dataScanner.next(); 

                Scanner refScanner = new Scanner(no);
                refScanner.useDelimiter("_");

                try {
                    String refNomor = refScanner.next();
                    String refTanggal = refScanner.next();
                    String refEntry = refScanner.next();

                    if (nomorBersih.equalsIgnoreCase(refNomor) && tanggal.equalsIgnoreCase(refTanggal)) {
                        entry = Long.parseLong(refEntry);
                    }
                } catch (NoSuchElementException | NumberFormatException ex) { }
            }
            data = bufferInput.readLine();
        }
        bufferInput.close();
        return entry;
    }

    private static boolean cekKonser(String[] keywords, boolean isDisplay) throws IOException {
        BufferedReader bufferInput = null;
    
        try {
            FileReader fileInput = new FileReader("database.txt");
            bufferInput = new BufferedReader(fileInput);
        } catch (IOException e) {
            errorUtil.handlingError(e);
            return false;
        }
    
        String data = bufferInput.readLine();
        boolean found = false;
        int nomorData = 0;
        int tiket = 0;
    
        if (isDisplay) {
            System.out.println("\n| No | Hari/Tanggal | Konser | Lokasi | Tiket Tersedia | Deskripsi |");
            System.out.println("--------------------------------------------------------------------------");
        }
    
        while (data != null) {
            boolean isMatch = true;
    
            for (String keyword : keywords) {
                isMatch = isMatch && data.toLowerCase().contains(keyword.toLowerCase());
            }
    
            if (isMatch) {
                found = true;
    
                if (isDisplay) {
                    nomorData++;
                    tiket++;
                    StringTokenizer st = new StringTokenizer(data, ",");
    
                    String id = st.nextToken();
                    String tanggal = st.nextToken();
                    String konser = st.nextToken();
                    String lokasi = st.nextToken();
                    String sisaTiket = st.nextToken();
                    String deskripsi = st.nextToken();
    
                    System.out.printf("| %2d | %-14s | %-12s | %-10s | %-15s | %s\n",
                            nomorData, tanggal, konser, lokasi, sisaTiket, deskripsi);
                } else {
                    break; 
                }
            }
    
            data = bufferInput.readLine();
        }
    
        if (isDisplay) {
            System.out.println("--------------------------------------------------------------------------");
        }
        bufferInput.close();
        return found;
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
        File tempDB = new File("tempDB.txt");

        BufferedReader bufferedInput = new BufferedReader(new FileReader(database));
        BufferedWriter bufferedOutput = new BufferedWriter(new FileWriter(tempDB));

        Scanner input = new Scanner(System.in);
        System.out.println("List Konser");
        // tampilkanKonser();

        System.out.print("\nMasukkan nomor konser yang ingin diubah: ");
        int updateNum = input.nextInt();
        input.nextLine();

        String data = bufferedInput.readLine();
        int entryCounts = 0;

        while (data != null) {
            entryCounts++;
            StringTokenizer st = new StringTokenizer(data, ",");

            if (updateNum == entryCounts) {
                String ref = st.nextToken();
                String nomor = st.nextToken();
                String tanggal = st.nextToken();
                String konser = st.nextToken();
                String lokasi = st.nextToken();
                String tiket = st.nextToken();
                String deskripsi = st.nextToken();

                System.out.println("\nData konser yang ingin diubah:");
                System.out.println("Nomor     : " + nomor);
                System.out.println("Tanggal   : " + tanggal);
                System.out.println("Konser    : " + konser);
                System.out.println("Lokasi    : " + lokasi);
                System.out.println("Tiket     : " + tiket);
                System.out.println("Deskripsi : " + deskripsi);

                String[] updated = new String[6];
                String[] fields = {nomor, tanggal, konser, lokasi, tiket, deskripsi};
                String[] fieldNames = {"Nomor", "Tanggal", "Konser", "Lokasi", "Tiket", "Deskripsi"};

                for (int i = 0; i < fields.length; i++) {
                    if (getYesorNo("Apakah ingin mengubah " + fieldNames[i] + "?")) {
                        System.out.print("Masukkan " + fieldNames[i] + " baru: ");
                        if (fieldNames[i].equalsIgnoreCase("Tanggal")) {
                            updated[i] = ambiltanggal();
                        } else {
                            updated[i] = input.nextLine();
                        }
                    } else {
                        updated[i] = fields[i];
                    }
                }

                boolean isUpdate = getYesorNo("Yakin ingin menyimpan perubahan?");
                if (isUpdate) {
                    bufferedOutput.write(ref + "," + String.join(",", updated));
                    bufferedOutput.newLine();
                    hapusTiketBerdasarkanKonser(konser);
                    try (BufferedWriter tiketWriter = new BufferedWriter(new FileWriter("tiket.txt", true))) {
                        int jumlahTiket = Integer.parseInt(updated[4]);
                        for (int i = 1; i <= jumlahTiket; i++) {
                            String idTiket = updated[0].replaceAll("\\s+", "") + "_" + updated[1].replaceAll("\\s+", "") + "_T" + i;
                            tiketWriter.write(idTiket + "," + updated[2] + ",50000,tersedia");
                            tiketWriter.newLine();
                        }
                    }
                    System.out.println("Data konser dan tiket berhasil diperbarui.");
                } else {
                    bufferedOutput.write(data);
                    bufferedOutput.newLine();
                }
            } else {
                bufferedOutput.write(data);
                bufferedOutput.newLine();
            }
            data = bufferedInput.readLine();
        }

        bufferedOutput.flush();
        bufferedOutput.close();
        bufferedInput.close();

        database.delete();
        tempDB.renameTo(database);
    }

    private static void hapusData() throws IOException {
        File database = new File("database.txt");
        File tempDB = new File("tempDB.txt");

        BufferedReader bufferedInput = null;
        BufferedWriter bufferedOutput = null;

        try {
            bufferedInput = new BufferedReader(new FileReader(database));
            bufferedOutput = new BufferedWriter(new FileWriter(tempDB));
        } catch (IOException e) {
            errorUtil.handlingError(e);
            return;
        }

        Scanner input = new Scanner(System.in);
        System.out.println("List Konser");
        // tampilkanKonser();

        System.out.print("\nMasukkan nomor konser yang ingin dihapus: ");
        int deleteNum = input.nextInt();
        input.nextLine();

        boolean isFound = false;
        int entryCounts = 0;
        String data = bufferedInput.readLine();

        while (data != null) {
            entryCounts++;
            boolean isDelete = false;
            StringTokenizer st = new StringTokenizer(data, ",");

            if (deleteNum == entryCounts) {
                System.out.println("\nKonser yang ingin Anda hapus adalah:");
                System.out.println("-----------------------------------");
                String ref = st.nextToken();
                String nomor = st.nextToken();
                String tanggal = st.nextToken();
                String konser = st.nextToken();
                System.out.println("Referensi  : " + ref);
                System.out.println("Nomor      : " + nomor);
                System.out.println("Tanggal    : " + tanggal);
                System.out.println("Konser     : " + konser);
                System.out.println("Lokasi     : " + st.nextToken());
                System.out.println("Tiket      : " + st.nextToken());
                System.out.println("Deskripsi  : " + st.nextToken());

                isDelete = getYesorNo("Apakah yakin ingin menghapus?");
                isFound = true;

                if (isDelete) {
                    hapusTiketBerdasarkanKonser(konser);
                    System.out.println("Data konser dan tiket berhasil dihapus.");
                    data = bufferedInput.readLine();
                    continue;
                }
            }

            bufferedOutput.write(data);
            bufferedOutput.newLine();
            data = bufferedInput.readLine();
        }

        if (!isFound) {
            System.err.println("Konser tidak ditemukan.");
        }

        bufferedOutput.flush();
        bufferedOutput.close();
        bufferedInput.close();

        database.delete();
        tempDB.renameTo(database);
    }

    private static void hapusTiketBerdasarkanKonser(String namaKonser) throws IOException {
        File tiketFile = new File("tiket.txt");
        File tempFile = new File("temp_tiket.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(tiketFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] f = line.split(",");
                if (f.length >= 2 && !f[1].equalsIgnoreCase(namaKonser)) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        }

        tiketFile.delete();
        tempFile.renameTo(tiketFile);
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

