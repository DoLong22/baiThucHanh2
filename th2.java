import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class SinhVien implements Serializable{
    private int masv;
    private String ten;
    private String lop;
    private double dtb;
    SinhVien(){}
    SinhVien(int masv, String ten, String lop, double dtb){
        this.masv = masv;
        this.lop = lop;
        this.ten = ten;
        this.dtb = dtb;
    }
    public int getMasv() {
        return masv;
    }

    public void setMasv(int masv) {
        this.masv = masv;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop;
    }

    public double getDtb() {
        return dtb;
    }

    public void setDtb(double dtb) {
        this.dtb = dtb;
    }
    public String XepLoai(){
        if(this.dtb >= 8)   return "Gioi";
        else if(this.dtb >=7 && this.dtb < 8)    return "Kha";
        else if(this.dtb < 7 && this.dtb >= 5)  return "Trung Binh";
        else    return "Yeu";
    }

    @Override
    public String toString(){
        return masv + " " + ten + " " + lop + " " + dtb;
    }
}
public class th2 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        List<SinhVien> listRead = new ArrayList<>();
        System.out.println("----------Menu----------");
        System.out.println("1.Doc file SV.INP");
        System.out.println("2.In ra man hinh");
        System.out.println("3.Sx theo lop + diem tb giam + ghi ra file SX.OUT");
        System.out.println("4.Xep loai + sap xep ten + ghi file XEPLOAI.OUT");
        System.out.println("0.Thoat");
        boolean end = false;
        while(true){
            int choose = in.nextInt();
            switch(choose){
                case 1:
                    readFile2("SV.INP");
                    break;
                case 2:
                    listRead = readFile2("SV.INP");
                    for(SinhVien sv: listRead)
                        System.out.println(sv.toString());
                    break;
                case 3:
                    listRead = readFile2("SV.INP");
                    Collections.sort(listRead,new sortDiem());
                    Collections.sort(listRead,new sortLop());
                    for(SinhVien sv: listRead)
                        System.out.println(sv.toString());
                    writeFile1("SX.OUT",listRead);
                    break;
                case 4:
                    listRead = readFile2("SV.INP");
                    Collections.sort(listRead,new sortTen());
                    writeFile2("XEPLOAI.OUT",listRead);
                    break;
                case 0:
                    end = true;
                    break;
            }
            if(end)    break;
        }
    }
    public static List<SinhVien> readFile(String path) {
        List<SinhVien> list = new ArrayList<>();
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        boolean end = true;
        try {
            fis = new FileInputStream(path);
            ois = new ObjectInputStream(fis);
            while (end) {
                SinhVien sv = (SinhVien) ois.readObject();
                if (sv != null) {
                    list.add(sv);
                }
                else end = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
    public static List<SinhVien> readFile2(String path){
        List<SinhVien> list = new ArrayList<>();
        Scanner in = null;
        try{
            in = new Scanner(new File(path));
            while(in.hasNext()){
                SinhVien sv = new SinhVien();
                int masv = in.nextInt();in.nextLine();
                sv.setMasv(masv);
                sv.setTen(in.nextLine());
                sv.setLop(in.nextLine());
                sv.setDtb(in.nextDouble());
                if(checkEx(sv)) list.add(sv);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            in.close();
        }
        return list;
    }
    public static void writeFile1(String path, List<SinhVien> list){
        PrintWriter sout = null;
        try{
            sout = new PrintWriter(new File(path));
            for(SinhVien sv: list)
                sout.println(sv.toString());
        }catch(FileNotFoundException e){
        } finally{
            sout.close();
        }
    }
    public static void writeFile2(String path, List<SinhVien> list){
        PrintWriter sout = null;
        try{
            sout = new PrintWriter(new File(path));
            for(SinhVien sv: list)
                sout.println(sv.XepLoai()+" "+ sv.toString());
        }catch(FileNotFoundException e){
        } finally{
            sout.close();
        }
    }
    public static boolean checkEx(SinhVien sv){
        checkException ex = new checkException();
        try{
            ex.checkMaSV(sv.getMasv());
            ex.checkTen(sv.getTen());
            ex.checkLop(sv.getLop());
            ex.checkDTB(sv.getDtb());
            return true;
        }catch(myEx ex1){
            return false;
        }
    }
}
class myEx extends Exception{}
class checkException{
    public void checkTen(String ten) throws myEx{
        if(ten.length() == 0)
            throw new myEx();
    }
    public void checkDTB(double dtb) throws myEx{
        if(dtb > 10 || dtb <0)
            throw new myEx();
    }
    public void checkMaSV(int masv) throws myEx{
        String s = String.valueOf(masv);
        if(s.length() != 4)
            throw new myEx();
    }
    public void checkLop(String lop) throws myEx{
        String regex = "[dD]\\d{2}[cCqQnNkKtTaAvT]{4}\\d{2}-[bB]";
        if(!lop.matches(regex))
            throw new myEx();
    }
}
class sortLop implements Comparator<SinhVien>{
    @Override
    public int compare(SinhVien sv1, SinhVien sv2){
        return sv1.getLop().compareToIgnoreCase(sv2.getLop());
    }
}
class sortDiem implements Comparator<SinhVien>{
    @Override
    public int compare(SinhVien sv1, SinhVien sv2){
        return (sv1.getDtb() > sv2.getDtb())? -1 : 1;
    }
}
class sortTen implements Comparator<SinhVien>{
    @Override
    public int compare(SinhVien sv1, SinhVien sv2){
        String[] sv1s = sv1.getTen().split(" ");
        String[] sv2s = sv2.getTen().split(" ");
        if(sv1s[sv1s.length-1].equals(sv2s[sv2s.length-1])){
            if(sv1s[0].equals(sv2s[0])){
                return sv1.getTen().compareToIgnoreCase(sv2.getTen());
            }else return sv1s[0].compareToIgnoreCase(sv2s[0]);
        }else return sv1s[sv1s.length-1].compareToIgnoreCase(sv2s[sv2s.length-1]);
    }
}
