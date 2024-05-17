/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.data.model.prop;

public class BankingInfo {
    private int id;
    private String bank;
    private String name;
    private String number;

    public BankingInfo() {
    }

    public BankingInfo(String bank, String name, String number) {
        this.bank = bank;
        this.name = name;
        this.number = number;
    }

    public BankingInfo(int id, String bank, String name, String number) {
        this.id = id;
        this.bank = bank;
        this.name = name;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public String getBank() {
        return bank;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "BankingInfo{" +
                "bank=" + bank +
                ", name=" + name +
                ", number=" + number +
                '}';
    }
}
