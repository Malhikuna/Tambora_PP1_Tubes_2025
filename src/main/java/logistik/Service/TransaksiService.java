package logistik.Service;

import logistik.Config.DatabaseConnection;
import logistik.Model.Transaksi;

import java.sql.Connection;

public class TransaksiService {
    /* Atribut */
    private StructureQueue transaksiQueue;
    private Connection conn;

    // Constructor
    public TransaksiService() {
        this.transaksiQueue = new StructureQueue();
        conn = DatabaseConnection.getConnection();
    }

    // Prosedur untuk menambahkan transaksi baru ke queue
    public void tambahTranksaksi(Transaksi transaksi) {
        transaksiQueue.enqueue(transaksi);
        System.out.println("Transaksi berhasil ditambahkan ke antrian.");
    }

    // Prosedur untuk menampilkan semua transaksi dalam queue
    public void tampilkanAntrian() {
        System.out.println("Daftar Transaksi dalam Antrian:");
        transaksiQueue.displayElement();
    }

    // Prosedur untuk menampilkan jumlah antrian queue
    public void jumlahAntrian() {
        System.out.println("Jumlah transaksi dalam antrian: " + transaksiQueue.size());
    }

    // Prosedur untuk melihat transaksi terdepan tanpa menghapus
    public void lihatAntrianTerdepan() {
        if (!transaksiQueue.isEmpty()) {
            Transaksi transaksi = (Transaksi) transaksiQueue.front();
            System.out.println("Transaksi terdepan: " + transaksi);
        } else {
            System.out.println("Antrian kosong.");
        }
    }
}
