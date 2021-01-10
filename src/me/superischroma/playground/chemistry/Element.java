package me.superischroma.playground.chemistry;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Element
{
    HYDROGEN("H", 1.008),
    HELIUM("He", 4.0026022),
    LITHIUM("Li", 6.94),
    BERYLLIUM("Be", 9.01218315),
    BORON("B", 10.81),
    CARBON("C", 12.011),
    NITROGEN("N", 14.007),
    OXYGEN("O", 15.999),
    FLUORINE("F", 18.9984031636),
    NEON("Ne", 20.17976),
    SODIUM("Na", 22.989769282),
    MAGNESIUM("Mg", 24.305),
    ALUMINIUM("Al", 26.98153857),
    SILICON("Si", 28.085),
    PHOSPHORUS("P", 30.9737619985),
    SULFUR("S", 32.06),
    CHLORINE("Cl", 35.45),
    ARGON("Ar", 39.9481),
    POTASSIUM("K", 39.09831),
    CALCIUM("Ca", 40.0784),
    SCANDIUM("Sc", 44.9559085),
    TITANIUM("Ti", 47.8671),
    VANADIUM("V", 50.94151),
    CHROMIUM("Cr", 51.99616),
    MANGANESE("Mn", 54.9380443),
    IRON("Fe", 55.8452),
    COBALT("Co", 58.9331944),
    NICKEL("Ni", 58.69344),
    COPPER("Cu", 63.5463),
    ZINC("Zn", 65.382),
    GALLIUM("Ga", 69.7231),
    GERMANIUM("Ge", 72.6308),
    ARSENIC("As", 74.9215956),
    SELENIUM("Se", 78.9718),
    BROMINE("Br", 79.904),
    KRYPTON("Kr", 83.7982),
    RUBIDIUM("Rb", 85.46783),
    STRONTIUM("Sr", 87.621),
    YTTRIUM("Y", 88.905842),
    ZIRCONIUM("Zr", 91.2242),
    NIOBIUM("Nb", 92.906372),
    MOLYBDENUM("Mo", 95.951),
    TECHNETIUM("Tc", 98),
    RUTHENIUM("Ru", 101.072),
    RHODIUM("Rh", 102.905502),
    PALLADIUM("Pd", 106.421),
    SILVER("Ag", 107.86822),
    CADMIUM("Cd", 112.4144),
    INDIUM("In", 114.8181),
    TIN("Sn", 118.7107),
    ANTIMONY("Sb", 121.7601),
    TELLURIUM("Te", 127.603),
    IODINE("I", 126.904473),
    XENON("Xe", 131.2936),
    CESIUM("Cs", 132.905451966),
    BARIUM("Ba", 137.3277),
    LANTHANUM("La", 138.905477),
    CERIUM("Ce", 140.1161),
    PRASEODYMIUM("Pr", 140.907662),
    NEODYMIUM("Nd", 144.2423),
    PROMETHIUM("Pm", 145),
    SAMARIUM("Sm", 150.362),
    EUROPIUM("Eu", 151.9641),
    GADOLINIUM("Gd", 157.253),
    TERBIUM("Tb", 158.925352),
    DYSPROSIUM("Dy", 162.5001),
    HOLMIUM("Ho", 164.930332),
    ERBIUM("Er", 167.2593),
    THULIUM("Tm", 168.934222),
    YTTERBIUM("Yb", 173.0451),
    LUTETIUM("Lu", 174.96681),
    HAFNIUM("Hf", 178.492),
    TANTALUM("Ta", 180.947882),
    TUNGSTEN("W", 183.841),
    RHENIUM("Re", 186.2071),
    OSMIUM("Os", 190.233),
    IRIDIUM("Ir", 192.2173),
    PLATINUM("Pt", 195.0849),
    GOLD("Au", 196.9665695),
    MERCURY("Hg", 200.5923),
    THALLIUM("Tl", 204.38),
    LEAD("Pb", 207.21),
    BISMUTH("Bi", 208.980401),
    POLONIUM("Po", 209),
    ASTATINE("At", 210),
    RADON("Rn", 222),
    FRANCIUM("Fr", 223),
    RADIUM("Ra", 226),
    ACTINIUM("Ac", 227),
    THORIUM("Th", 232.03774),
    PROTACTINIUM("Pa", 231.035882),
    URANIUM("U", 238.028913),
    NEPTUNIUM("Np", 237),
    PLUTONIUM("Pu", 244),
    AMERICIUM("Am", 243),
    CURIUM("Cm", 247),
    BERKELIUM("Bk", 247),
    CALIFORNIUM("Cf", 251),
    EINSTEINIUM("Es", 252),
    FERMIUM("Fm", 257),
    MENDELEVIUM("Md", 258),
    NOBELIUM("No", 259),
    LAWRENCIUM("Lr", 266),
    RUTHERFORDIUM("Rf", 267),
    DUBNIUM("Db", 268),
    SEABORGIUM("Sg", 269),
    BOHRIUM("Bh", 270),
    HASSIUM("Hs", 269),
    MEITNERIUM("Mt", 278),
    DARMSTADTIUM("Ds", 281),
    ROENTGENIUM("Rg", 282),
    COPERNICIUM("Cn", 285),
    NIHONIUM("Nh", 286),
    FLEROVIUM("Fl", 289),
    MOSCOVIUM("Mc", 289),
    LIVERMORIUM("Lv", 293),
    TENNESSINE("Ts", 294),
    OGANESSON("Og", 294),
    UNUNENNIUM("Uue", 315);

    private final String symbol;
    private final double mass;

    Element(String symbol, double mass)
    {
        this.symbol = symbol;
        this.mass = mass;
    }

    public double getAtomicMass()
    {
        return mass;
    }

    public String getSymbol()
    {
        return symbol;
    }

    public int getAtomicNumber()
    {
        return ordinal() + 1;
    }

    public static Element getByName(String name)
    {
        try
        {
            return valueOf(name);
        }
        catch (IllegalArgumentException ex)
        {
            return null;
        }
    }

    public static Element getBySymbol(String symbol)
    {
        List<Element> results = Arrays.stream(values()).filter((element) ->
                element.getSymbol().equals(symbol)).collect(Collectors.toList());
        return results.size() != 0 ? results.get(0) : null;
    }
}