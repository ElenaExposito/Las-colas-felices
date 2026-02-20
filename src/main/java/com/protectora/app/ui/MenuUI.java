@Component
public class MenuUI implements CommandLineRunner {
    @Autowired private AnimalService animalService;
    @Autowired private ObjectMapper objectMapper; // Jackson

    @Override
    public void run(String... args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("--- SISTEMA LAS COLAS ALEGRES ---");
        
        // Ejemplo de flujo CRUD + Export
        System.out.println("1. Listar Animales | 2. Exportar JSON | 0. Salir");
        int opcion = sc.nextInt();
        
        if (opcion == 1) {
            animalService.listarTodos().forEach(a -> 
                System.out.println(a.getNombre() + " - " + a.getEstado()));
        } else if (opcion == 2) {
            // Serialización Jackson
            String json = objectMapper.writerWithDefaultPrettyPrinter()
                                      .writeValueAsString(animalService.listarTodos());
            System.out.println("JSON Generado:\n" + json);
        }
    }
}