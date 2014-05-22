package yuanye.dp;

/**
 * Design Pattern 2:Builder.
 * Using when create complex object(immutable object as well),using a builder to build each part of
 * the object and a director to direct it.
 */
public class Builder {
    static class Phone{
        private final String cpu;
        private final String gpu;
        private final long memory;
        private final long disk;
        private final String screen;
        private final String brand;

        private Phone(PhoneBuilder builder){
            this.brand = builder.brand;
            this.cpu = builder.cpu;
            this.memory = builder.memory;
            this.gpu = builder.gpu;
            this.disk = builder.disk;
            this.screen = builder.screen;
        }
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(" brand :")
                    .append(brand)
                    .append(" cpu:")
                    .append(cpu)
                    .append(" gpu:")
                    .append(gpu)
                    .append(" screen:")
                    .append(screen)
                    .append(" memory:")
                    .append(memory)
                    .append(" disk:")
                    .append(disk);
            return sb.toString();
        }

        public static class PhoneBuilder{
            private  String cpu = "mtk";
            private  String gpu = "mtk";
            private  long memory = 1024*1024*512;   //512M
            private  long disk = 1024*1024*2048;    //2G
            private  String screen ="800*480" ;
            private  String brand ;

            public PhoneBuilder(String brand){
                this.brand = brand;
            }

            public PhoneBuilder setCpu(String cpu){
                this.cpu = cpu;
                return this;
            }

            public PhoneBuilder setGpu(String gpu){
                this.gpu = gpu;
                return this;
            }

            public PhoneBuilder setMemory(long memory){
                this.memory = memory;
                return this;
            }

            public PhoneBuilder setDisk(long disk){
                this.disk = disk;
                return this;
            }

            public PhoneBuilder setScreen(String screen){
                this.screen = screen;
                return this;
            }

            public Phone build(){
                return new Phone(this);
            }


        }
    }

    public static void main(String[] args){
        Phone phone = new Phone.PhoneBuilder("Meizu MX2")
                .setCpu("MX5S")
                .setDisk(1024*1024*1024*32)
                .setMemory(1024*1024*2048)
                .setScreen("960*640")
                .setGpu("MX5S")
                .build();
        System.out.println(phone);

    }
}
