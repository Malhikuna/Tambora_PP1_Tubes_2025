package logistik.model;

public class Barang {
    /* Atribut */
    private String kode;
    private String nama;
    private int kategoriId;
    private String satuan;
    private String namaKategori;
    private double hargaBeli;
    private double hargaJual;

    /* Constructor */
    public Barang(String kode, String nama, int kategoriId, String satuan, String namaKategori ,double hargaBeli, double hargaJual) {
        this.kode = kode;
        this.nama = nama;
        this.satuan = satuan;
        this.kategoriId = kategoriId;
        this.namaKategori = namaKategori;
        this.hargaBeli = hargaBeli;
        this.hargaJual = hargaJual;
    }



    /* Getter & Setter */
    public String getKode() {
        return kode;
    }

    public String getNama() {
        return nama;
    }


    public int getKategoriId() {
        return kategoriId;
    }

    public String getSatuan() {
        return satuan;
    }

    public String getNamaKategori() {
        return namaKategori;
    }

    public double getHargaBeli() {
        return hargaBeli;
    }

    public double getHargaJual() {
        return hargaJual;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }


    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setKategoriId (int kategoriId) {
        this.kategoriId = kategoriId;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public void setNamaKategori (String namaKategori) {
        this.namaKategori = namaKategori;
    }

    public void setHargaBeli(double harga_beli) {
        this.hargaBeli = harga_beli;
    }

    public void setHargaJual(double harga_jual) {
        System.out.println(harga_jual + "Ko");
        this.hargaJual = harga_jual;
    }

    // Menampilkan kode, nama, dan stok barang
    public void tampilBarang() {
        System.out.println("Kode Barang : " +kode);
        System.out.println("Nama Barang : " +nama);
        System.out.println("Kategori Barang : " +kategoriId);
        System.out.println("Satuan Barang : " +satuan);
        System.out.println("Harga Beli Barang : " +hargaBeli);
        System.out.println("Harga Jual Barang : " +hargaJual);
    }
}
