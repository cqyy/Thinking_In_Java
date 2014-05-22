package yuanye.Annotation;

/**
 * Created by Administrator on 14-3-10.
 */
public class PassworUnti {

    @UseCase(id = 12, description = "validate password")
    public boolean validatePassword(){
        return false;
    }

    @UseCase(id = 13)
    public String encryptPassword(){
        return "";
    }

    @UseCase(id = 14, description = "New password couldn't equal previously used ones")
    public boolean checkForNewPassword(){
        return false;
    }
}
