package logistik.Model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaksi {
    /* Atribut */
    private UUID id;
    private String barangId;
    private String tipe;
    private int jumlah;
    private String supplierId;
    private String penerimaId;
    private String userId;
    private LocalDateTime waktu;

    /* Constructor */
    public Transaksi(UUID id, String barangId, String tipe, int jumlah, String supplierId, String penerimaId,
                     String userId) {
        this.id = UUID.randomUUID(); // Generate a random UUID if not provided
        this.barangId = barangId;
        this.tipe = tipe;
        this.jumlah = jumlah;
        this.supplierId = supplierId;
        this.penerimaId = penerimaId;
        this.userId = userId;
        this.waktu = LocalDateTime.now();
    }

    /* Getter & Setter */
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBarangId() {
        return barangId;
    }

    public void setBarangId(String barangId) {
        this.barangId = barangId;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getPenerimaId() {
        return penerimaId;
    }

    public void setPenerimaId(String penerimaId) {
        this.penerimaId = penerimaId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getWaktu() {
        return waktu;
    }

    public void setWaktu(LocalDateTime waktu) {
        this.waktu = waktu;
    }
}