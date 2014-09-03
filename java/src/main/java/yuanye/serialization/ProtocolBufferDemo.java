package yuanye.serialization;

/**
 * Created by Administrator on 2014/9/3.
 */
public class ProtocolBufferDemo {

    public static void main(String[] args) {
        AddressBookProtos.Person john =
                AddressBookProtos.Person.newBuilder()
                        .setId(1234)
                        .setName("John Doe")
                        .setEmail("jdoe@example.com")
                        .addPhone(
                                AddressBookProtos.Person.PhoneNumber.newBuilder()
                                        .setNumber("555-4321")
                                        .setType(AddressBookProtos.Person.PhoneType.HOME))
                        .build();
        System.out.println(john);
    }
}
