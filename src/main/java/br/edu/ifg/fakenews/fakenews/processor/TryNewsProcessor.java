package br.edu.ifg.fakenews.fakenews.processor;

import br.edu.ifg.fakenews.fakenews.domain.Tb001News;
import br.edu.ifg.fakenews.fakenews.domain.Tb003ProcessedNews;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TryNewsProcessor implements ItemProcessor<Tb001News, Tb003ProcessedNews> {

    private final ResourceLoader resourceLoader;

    private final Set<String> stopwords = new HashSet<>(Arrays.asList(
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
            "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "ao", "aos", "aquela", "aquelas", "aquele", "aqueles", "aquilo", "as",
            "até", "com", "como", "da", "das", "de", "dela", "delas", "dele", "deles",
            "depois", "do", "dos", "e", "ela", "elas", "ele", "eles", "em", "entre",
            "era", "eram", "essa", "essas", "esse", "esses", "esta", "esta", "estas",
            "este", "estes", "eu", "isso", "isto", "já", "lhe", "lhes", "mais", "mas",
            "me", "mesmo", "meu", "meus", "minha", "minhas", "muito", "na", "não", "nas",
            "nem", "no", "nos", "nossa", "nossas", "nosso", "nossos", "num", "numa", "nuns",
            "nunca", "o", "os", "ou", "para", "pela", "pelas", "pelo", "pelos", "por", "que",
            "quem", "se", "seja", "sejam", "sem", "ser", "seu", "seus", "so", "sua", "suas",
            "tambem", "te", "tem", "tinha", "tinham", "toda", "todas", "todo", "todos", "um",
            "uma", "umas", "uns", "você", "vocês", "vos", "como", "dos", "o", "em", "na", "e",
            "a", "as", "R$", "sim", "nao", "o", "u", "no", "os", "por", "a", "foi", "sao", "sobre",
            "presidente", "ja", "em",
            "a", "ao", "aos", "aquela", "aquelas", "aquele", "aqueles", "aquilo", "as",
            "até", "com", "como", "da", "das", "de", "dela", "delas", "dele", "deles",
            "depois", "do", "dos", "e", "ela", "elas", "ele", "eles", "em", "entre",
            "era", "eram", "essa", "essas", "esse", "esses", "esta", "esta", "estas",
            "este", "estes", "eu", "isso", "isto", "já", "lhe", "lhes", "mais", "mas",
            "me", "mesmo", "meu", "meus", "minha", "minhas", "muito", "na", "não", "nas",
            "nem", "no", "nos", "nossa", "nossas", "nosso", "nossos", "num", "numa", "nuns",
            "nunca", "o", "os", "ou", "para", "pela", "pelas", "pelo", "pelos", "por", "que",
            "quem", "se", "seja", "sejam", "sem", "ser", "seu", "seus", "so", "sua", "suas",
            "tambem", "te", "tem", "tinha", "tinham", "toda", "todas", "todo", "todos", "um",
            "uma", "umas", "uns", "você", "vocês", "vos", "como", "dos", "o", "em", "na", "e",
            "a", "as", "R$", "sim", "nao", "o", "u", "no", "os", "por",
            "de", "a", "o", "que", "e", "do", "da", "em", "um", "para", "é", "com", "não", "uma",
            "os", "no", "se", "na", "por", "mais", "as", "dos", "como", "mas", "foi", "ao", "ele",
            "das", "tem", "à", "seu", "sua", "ou", "ser", "quando", "muito", "há", "nos", "já",
            "está", "eu", "também", "só", "pelo", "pela", "até", "isso", "ela", "entre", "era",
            "depois", "sem", "mesmo", "aos", "ter", "seus", "quem", "nas", "me", "esse", "eles",
            "estão", "você", "tinha", "foram", "essa", "num", "nem", "suas", "meu", "às", "minha",
            "têm", "numa", "pelos", "elas", "havia", "seja", "qual", "será", "nós", "tenho", "lhe",
            "deles", "essas", "esses", "pelas", "este", "fosse", "dele", "tu", "te", "vocês", "vos",
            "lhes", "meus", "minhas", "teu", "tua", "teus", "tuas", "nosso", "nossa", "nossos",
            "nossas", "dela", "delas", "esta", "estes", "estas", "aquele", "aquela", "aqueles",
            "aquelas", "isto", "aquilo", "estou", "está", "estamos", "estão", "estive", "esteve",
            "estivemos", "estiveram", "estava", "estávamos", "estavam", "estivera", "estivéramos",
            "esteja", "estejamos", "estejam", "estivesse", "estivéssemos", "estivessem", "estiver",
            "estivermos", "estiverem", "hei", "há", "havemos", "hão", "houve", "houvemos", "houveram",
            "houvera", "houvéramos", "haja", "hajamos", "hajam", "houvesse", "houvéssemos", "houvessem",
            "houver", "houvermos", "houverem", "houverei", "houverá", "houveremos", "houverão",
            "houveria", "houveríamos", "houveriam", "sou", "somos", "são", "era", "éramos", "eram",
            "fui", "foi", "fomos", "foram", "fora", "fôramos", "seja", "sejamos", "sejam", "fosse",
            "fôssemos", "fossem", "for", "formos", "forem", "serei", "será", "seremos", "serão",
            "seria", "seríamos", "seriam", "tenho", "tem", "temos", "tém", "tinha", "tínhamos", "tinham",
            "tive", "teve", "tivemos", "tiveram", "tivera", "tivéramos", "tenha", "tenhamos", "tenham",
            "tivesse", "tivéssemos", "tivessem", "tiver", "tivermos", "tiverem", "terei", "terá",
            "teremos", "terão", "teria", "teríamos", "teriam", "ex", "feira"
    ));

    @Override
    public Tb003ProcessedNews process(Tb001News item) {
        Tb003ProcessedNews tb003ProcessedNews = new Tb003ProcessedNews();
        tb003ProcessedNews.setStatus(item.getStatusNews().equals("TRUE") ? 1 : 0);
        tb003ProcessedNews.setNews(tryNews(item.getId(), item.getNews()));
        return tb003ProcessedNews;
    }

    private String tryNews(Long id, String news) {
        String normalizedText = removeAccents(news);
        String[] words = normalizedText.split("\\s+|\\p{Pd}+");

        String modelPath = "classpath:pt-token.bin";

        try {
            // Carrega o arquivo de modelo de tokenização
            Resource resource = resourceLoader.getResource(modelPath);
            InputStream modelIn = resource.getInputStream();
            TokenizerModel model = new TokenizerModel(modelIn);

            // Cria um tokenizer com o modelo carregado
            Tokenizer tokenizer = new TokenizerME(model);

            Set<String> cleanedStopWordsFull = stopwords.stream().map(TryNewsProcessor::removeAccents).collect(Collectors.toSet());

            // Tokeniza a string de entrada
            String[] tokens = tokenizer.tokenize(normalizedText);
            List<String> cleanedStopWords = Arrays.stream(tokens)
                    .filter(word -> !word.isEmpty() && !cleanedStopWordsFull.contains(word))
                    .toList();

            // Filtra tokens para manter apenas palavras
            List<String> cleanedWordsList = cleanedStopWords.stream()
                    .map(word -> word.replaceAll("[^A-Za-zÀ-ÿ]", " ").toLowerCase().trim())
                    .filter(word -> !word.isEmpty())
                    .collect(Collectors.toList());

            log.info("[{}] Saida: {}", id, cleanedWordsList);

            return String.join(" ", cleanedWordsList);
        } catch (IOException e) {
            log.error("error no tratamento dos textos", e);
        }
        return null;
    }

    public static String removeAccents(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replace("&amp", " ").
                replace("&lt;", " ").
                replace("&gt;", " ").
                replace("&apos;", " ").
                replace("&quot;", " ").
                replace("&;", " ").replace("\\", " ");
        str = str.replaceAll("[^\\p{ASCII}]", "").replaceAll("\\b-\\b", " ");
        return str.toLowerCase();
    }

}
