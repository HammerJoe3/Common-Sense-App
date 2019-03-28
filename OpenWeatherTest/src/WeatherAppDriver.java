public class WeatherAppDriver
{
    public static void main(String[] args)
    {
        WeatherApp wapp = new WeatherApp("08562");

        System.out.println(wapp.getMain());
        System.out.println(wapp.getTemperature());
    }

}
