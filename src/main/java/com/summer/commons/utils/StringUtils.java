package com.summer.commons.utils;

import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe com utilitários relacionado a {@link String}.
 */
public class StringUtils {
  
  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###");
  private static final DecimalFormat DECIMAL_FORMAT2 = new DecimalFormat("#,###.0");
  private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("#.##");
  private static final Pattern COLOR_PATTERN = Pattern.compile("(?i)(§)[0-9A-FK-OR]");
  private static final NavigableMap<Double, String> suffixes = new TreeMap<>();

  static {
    suffixes.put(1_000D, "k");
    suffixes.put(1_000_000D, "M");
    suffixes.put(1_000_000_000D, "B");
    suffixes.put(1_000_000_000_000D, "T");
    suffixes.put(1_000_000_000_000_000D, "Q");
    suffixes.put(1_000_000_000_000_000_000D, "QQ");
    suffixes.put(1_000_000_000_000_000_000_000D, "S");
    suffixes.put(1_000_000_000_000_000_000_000_000D, "SS");
    suffixes.put(1_000_000_000_000_000_000_000_000_000D, "OC");
    suffixes.put(1_000_000_000_000_000_000_000_000_000_000D, "N");
    suffixes.put(1_000_000_000_000_000_000_000_000_000_000_000D, "D");
    suffixes.put(1_000_000_000_000_000_000_000_000_000_000_000_000D, "UN");
    suffixes.put(1_000_000_000_000_000_000_000_000_000_000_000_000_000D, "DD");
    suffixes.put(1_000_000_000_000_000_000_000_000_000_000_000_000_000_000D, "TR");
    suffixes.put(1_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000D, "QT");
    suffixes.put(1_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000D, "QN");
    suffixes.put(1_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000D, "SD");
    suffixes.put(1_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000D, "SPD");
    suffixes.put(1_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000D, "OD");
    suffixes.put(1_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000D, "ND");
    suffixes.put(1_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000D, "VG");
    suffixes.put(1_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000D, "UVG");
    suffixes.put(1_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000D, "DVG");
    suffixes.put(1_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000D, "TVG");
    suffixes.put(1_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000D, "QTV");
    suffixes.put(1_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000D, "QNV");
    suffixes.put(1_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000D, "SEV");
    suffixes.put(1_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000D, "SPV");
    suffixes.put(1_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000D, "OVG");
    suffixes.put(1_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000D, "NVG");
    suffixes.put(1_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000D, "TG");
  }

  public static String transformTimeFormated(double time) {
    int segunds;
    int minuts = 0;
    int hours = 0;
    int days = 0;
    int monthys = 0;
    int years = 0;

    while (time >= 31104000) {
      years++;
      time -= 31104000;
    }

    while (time >= 2592000) {
      monthys++;
      time -= 2592000;
    }

    while (time >= 86400) {
      days++;
      time -= 86400;
    }

    while (time >= 3600) {
      hours++;
      time -= 3600;
    }

    while (time >= 60) {
      minuts++;
      time -= 60;
    }

    segunds = (int) time;
    return (years > 0 ? years + "a " : "") + (monthys > 0 ? monthys + "m " : "") + (days > 0 ? days + "d " : "") + (hours > 0 ? hours + "h " : "") + (minuts > 0 ? minuts + "m " : "") + (segunds > 0 ? segunds + "s " : "");
  }

  public static String format(double value) {
    if (value == Double.MIN_VALUE) {
      return format(Double.MIN_VALUE + 1);
    }

    if (value < 0) {
      return "-" + format(-value);
    }

    if (value < 1000) {
      return Double.toString(value);
    }

    Map.Entry<Double, String> e = suffixes.floorEntry(value);
    Double divideBy = e.getKey();
    String suffix = e.getValue();

    double truncated = value / (divideBy / 10);
    boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
    return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
  }

  public static Double formatNumber(String numberString) {
    double number = 0D;
    StringBuilder typeMultiply = null;

    for (char caracter : numberString.toCharArray()) {
      try {
        Double.parseDouble(String.valueOf(caracter));
      } catch (Exception e) {
        if (typeMultiply == null) {
          typeMultiply = new StringBuilder(String.valueOf(caracter));
        } else {
          typeMultiply.append(caracter);
        }
      }
    }

    if (typeMultiply != null) {
      for (Double types : suffixes.keySet()) {
        if (suffixes.get(types).equalsIgnoreCase(typeMultiply.toString())) {
          number = Double.parseDouble(numberString.replace(typeMultiply.toString().toUpperCase(Locale.ROOT), "")) * types;
        }
      }
    } else {
      number = Double.parseDouble(numberString);
    }

    return number;
  }
  
  /**
   * Formata um número "#,###" através do {@link DecimalFormat}
   *
   * @param number Para formatar.
   * @return O número formatado.
   */
  public static String formatNumber(int number) {
    return DECIMAL_FORMAT.format(number);
  }
  
  /**
   * Formata um número "#,###" através do {@link DecimalFormat}
   *
   * @param number Para formatar.
   * @return O número formatado.
   */
  public static String formatNumber(long number) {
    return DECIMAL_FORMAT.format(number);
  }
  
  /**
   * Formata um número "#,###" através do {@link DecimalFormat}
   *
   * @param number Para formatar.
   * @return O número formatado.
   */
  public static String formatNumber(double number) {
    return DECIMAL_FORMAT.format(number);
  }
  
  /**
   * Remove todas as cores de uma String.<br/>
   * Color code: §
   *
   * @param input A string para remover as cores.
   * @return A string sem cores.
   */
  public static String stripColors(final String input) {
    if (input == null) {
      return null;
    }
    
    return COLOR_PATTERN.matcher(input).replaceAll("");
  }
  
  /**
   * Formata os {@code &} para o color code {@code §}.
   *
   * @param textToFormat A string para formatar as cores.
   * @return A string com as cores formatadas.
   */
  public static String formatColors(String textToFormat) {
    return translateAlternateColorCodes('&', textToFormat);
  }
  
  /**
   * Desformata o color code {@code §} para {@code &}.
   *
   * @param textToDeFormat A string para desformatar as cores.
   * @return A string com as cores desformatadas.
   */
  public static String deformatColors(String textToDeFormat) {
    Matcher matcher = COLOR_PATTERN.matcher(textToDeFormat);
    while (matcher.find()) {
      String color = matcher.group();
      textToDeFormat = textToDeFormat.replaceFirst(Pattern.quote(color), Matcher.quoteReplacement("&" + color.substring(1)));
    }
    
    return textToDeFormat;
  }
  
  /**
   * Formata os {@link} para o color code {@code §}.
   *
   * @param altColorChar    O caractere que é definido como color code.
   * @param textToTranslate A string para formatar as cores.
   * @return A string com as cores formatadas.
   */
  public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
    Pattern pattern = Pattern.compile("(?i)(" + altColorChar + ")[0-9A-FK-OR]");
    
    Matcher matcher = pattern.matcher(textToTranslate);
    while (matcher.find()) {
      String color = matcher.group();
      textToTranslate = textToTranslate.replaceFirst(Pattern.quote(color), Matcher.quoteReplacement("§" + color.substring(1)));
    }
    
    return textToTranslate;
  }
  
  /**
   * Pega a primeira cor de uma {@code String}.
   *
   * @param input A string para pegar a cor.
   * @return A primeira cor ou {@code ""(vazio)} caso não encontre nenhuma.
   */
  public static String getFirstColor(String input) {
    Matcher matcher = COLOR_PATTERN.matcher(input);
    String first = "";
    if (matcher.find()) {
      first = matcher.group();
    }
    
    return first;
  }
  
  /**
   * Pega a última cor de uma {@code String}.
   *
   * @param input A string para pegar a cor.
   * @return A última cor ou {@code ""(vazio)} caso não encontre nenhuma.
   */
  public static String getLastColor(String input) {
    Matcher matcher = COLOR_PATTERN.matcher(input);
    String last = "";
    while (matcher.find()) {
      last = matcher.group();
    }
    
    return last;
  }
  
  /**
   * Repete uma String várias vezes.
   *
   * @param repeat A string para repetir.
   * @param amount A quantidade de vezes que será repetida.
   * @return Resultado da repetição.
   */
  public static String repeat(String repeat, int amount) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < amount; i++) {
      sb.append(repeat);
    }
    
    return sb.toString();
  }
  
  /**
   * Junta uma Array em uma {@code String} utilizando um separador.
   *
   * @param array     A array para juntar.
   * @param index     O ínicio da iteração da array (0 = toda a array).
   * @param separator O separador da junção.
   * @return Resultado da junção.
   */
  public static <T> String join(T[] array, int index, String separator) {
    StringBuilder joined = new StringBuilder();
    for (int slot = index; slot < array.length; slot++) {
      joined.append(array[slot].toString()).append(slot + 1 == array.length ? "" : separator);
    }
    
    return joined.toString();
  }
  
  /**
   * Junta uma Array em uma {@code String} utilizando um separador.
   *
   * @param array     A array para juntar.
   * @param separator O separador da junção.
   * @return Resultado da junção.
   */
  public static <T> String join(T[] array, String separator) {
    return join(array, 0, separator);
  }
  
  /**
   * Junta uma Coleção em uma {@code String} utilizando um separador.
   *
   * @param collection A coleção para juntar.
   * @param separator  O separador da junção.
   * @return Resultado da junção.
   */
  public static <T> String join(Collection<T> collection, String separator) {
    return join(collection.toArray(new Object[0]), separator);
  }
  
  /**
   * Quebra uma {@code String} várias vezes para criar linhas com o tamanho máximo definido.<br/>
   * <b>Observação:</b> Esse método é uma variação do {@link StringUtils#split(String, int, boolean)}
   * com o parâmetro {@code ignoreCompleteWords} definido como {@code false}.
   *
   * @param toSplit String para quebrar.
   * @param length  Tamanho máximo das linhas.
   * @return Resultado da separação.
   */
  public static String[] split(String toSplit, int length) {
    return split(toSplit, length, false);
  }
  
  /**
   * "Capitaliza" uma String Exemplo: MAXTER se torna Maxter
   *
   * @param toCapitalise String para capitalizar
   * @return Resultado capitalizado.
   */
  public static String capitalise(String toCapitalise) {
    StringBuilder result = new StringBuilder();
    
    String[] splittedString = toCapitalise.split(" ");
    for (int index = 0; index < splittedString.length; index++) {
      String append = splittedString[index];
      result.append(append.substring(0, 1).toUpperCase()).append(append.substring(1).toLowerCase()).append(index + 1 == splittedString.length ? "" : " ");
    }
    
    return result.toString();
  }
  
  
  /**
   * Quebra uma {@code String} várias vezes para criar linhas com o tamanho máximo definido.
   *
   * @param toSplit               String para quebrar.
   * @param length                Tamanho máximo das linhas.
   * @param ignoreIncompleteWords Se irá ignorar a quebra de palavras ou não (caso esteja desativado e
   *                              for quebrar uma palavra, ela irá ser removida da linha atual e adicionar na próxima).
   * @return Resultado da separação.
   */
  public static String[] split(String toSplit, int length, boolean ignoreIncompleteWords) {
    StringBuilder result = new StringBuilder(), current = new StringBuilder();
    
    char[] arr = toSplit.toCharArray();
    for (int charId = 0; charId < arr.length; charId++) {
      char character = arr[charId];
      if (current.length() == length) {
        if (!ignoreIncompleteWords) {
          List<Character> removedChars = new ArrayList<>();
          for (int l = current.length() - 1; l > 0; l--) {
            if (current.charAt(l) == ' ') {
              current.deleteCharAt(l);
              result.append(current).append("\n");
              Collections.reverse(removedChars);
              current = new StringBuilder(join(removedChars, ""));
              break;
            }
            
            removedChars.add(current.charAt(l));
            current.deleteCharAt(l);
          }
          
          removedChars.clear();
          removedChars = null;
        } else {
          result.append(current).append("\n");
          current = new StringBuilder();
        }
      }
      
      current.append(current.length() == 0 && character == ' ' ? "" : character);
      if (charId + 1 == arr.length) {
        result.append(current).append("\n");
      }
    }
    
    return result.toString().split("\n");
  }

  public static String getRandomCaracter(Integer caracters) {
    Random r = new Random();
    List<String> b = new ArrayList<>();
    for (int i = 0; i < caracters; i++) {
      char c = (char)(r.nextInt(26) + 'a');
      b.add(String.valueOf(c));
    }
    StringJoiner joiner = new StringJoiner("");
    for (String s : b) {
      joiner.add(s);
    }
    String str = joiner.toString();
    return String.valueOf(str);
  }

  public static String getRandomCode(Integer caracters) {
    Random r = new Random();
    List<String> b = new ArrayList<>();
    for (int i = 0; i < caracters; i++) {
      char c = (char)(r.nextInt(9) + '1');
      b.add(String.valueOf(c));
    }
    StringJoiner joiner = new StringJoiner("");
    for (String s : b) {
      joiner.add(s);
    }
    String str = joiner.toString();
    return String.valueOf(str);
  }

  public static boolean isInvalid(double value) {
    return value < 0 || Double.isNaN(value) || Double.isInfinite(value);
  }

  public static String formatNumber(double number, boolean isTwo) {
    if (number == 0.0) {
      return "0.0";
    }
    return DECIMAL_FORMAT2.format(number);
  }

  public static String formatNumber2(double number) {
    return NUMBER_FORMAT.format(number);
  }
}
