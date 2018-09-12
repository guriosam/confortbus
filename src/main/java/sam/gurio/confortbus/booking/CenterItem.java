package sam.gurio.confortbus.booking;

public class CenterItem extends AbstractItem {

    public CenterItem(String label) {
        super(label);
    }

    @Override
    public int getType() {
        return TYPE_CENTER;
    }

}