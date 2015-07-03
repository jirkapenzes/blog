---
title: Začátky s Clojure očima OOP programátora
publish-date: 29.06.2015
tags: dev, clojure
---

Článek je orientovaný zejména na prvotní zkušenosti s kódem napsaným v Clojure.
Celá platforma přináší nespočet výhod a zajímavých vlastností,
které stojí za to poznat. Jaké jsou ale začátky?

Ještě před rokem jsem byl silný zastánce objektově orientovaného programování. Nebyl
jsem přímo odpůrce funkcionálního přístupu, ale nelíbila se mi ta šílená bublina kolem
toho. Ve své podstatě je OOP dobrý model, nicméně je až příliš volný a často (téměř vždy)
špatně uchopený. Některé prvky z funkcionálního světa začínají silně ovlivňovat OOP
komunitu - kupříkladu aktuálně všudypřítomná immutabilta. Ten “jiný” svět mě začínal
čím dál více fascinovat. Nemá smysl psát v objektově orientovaném jazyce funkcionálně,
když máme funkcionální jazyky. Proto jsem dal šanci Clojure.

## Proč Clojure?

Protože frčí nad JVM a je to takový Java friendly jazyk (myšleno tak, že žijí společně
v určité symbióze). Pokud máte raději .Net svět, určitě  spíš sáhněte po F#. V mém
případě měli určitý vliv při výběru Clojure i pánové [Aleš Roubíček](https://twitter.com/alesroubicek)
a [Marian Schubert](https://twitter.com/marianschubert), kteří mi udělali v některých věcech zásadní osvětu.

## Začátky

Je to zlo. WTF moment naroste do úplně jíných rozměrů. Prostě peklo. Problém je zejména
čtení kódu. Clojure jsem viděl poprvé asi před třemi lety. Tehdy mi to připomínalo kód,
kterému může rozumnět snad jen kompilátor. Není šance, aby tomuhle nějaký člověk porozumněl.
Natož, aby to někdo dokázal takhle zapsat. Po nějaké době se naše cesty opět protnuli -
konkrétně na [Global Day of Code Retreat](http://coderetreat.cz/). Jen jsem tiše seděl a
nechápavě koukal, jak kolega zběsile píše jednu závorku za druhou. Další mé setkání s
Clojure proběhlo na pražském [Coding Dojo](http://codingdojo.cz). Aleš s Mariánem nám
tehdy cosi předváděli. Postupně jsme zkoušeli i další funkcionální jazyky. Tím se mi
začal lehce otevírat funkcionální svět a tehdy jsem se rozhodl, že to tedy taky zkusím
a dám tomu šanci.

## Výběr IDE

Prvním krokem je správný výběr vývojového prostředí. Udělal jsem malý research a snažil
se zjistit, které IDE se v Clojure nejvíce používá a je považováno za nejlepší.
Toto mi vyšlo (1 = nejvíc):

1. Emacs,
2. VIM,
3. Cursive (plugin do IntelliJ IDEA).

Hmmm. Na rovinu - Emacs a VIM jsou asi nejvíc boží a cool. Nicméně už takhle je
funkcionální svět příliš neznámý a proto není úplně ideální  zabíjet čas studováním
i zcela nového (a diametrálně odlišného) vývojového prostředí. Další často zvolenou
volbou je Cursive. Tehdy se mi to jevilo jako příliš velký kanón na něco tak simple,
jako je Clojure. Hledání pokračovalo.

Dalších IDE a pluginů je celá řada. Můžete ohnout a používat většinu současných chytrých
textových editorů alá Sublime Text, apod. Za sebe nakonec doporučuji mladý
[Light Table](http://lighttable.com/). Je to asi nejlepší entry point do tohoto světa.
Light Table je mimochodem sám napsán v ClojureScriptu a je přímo určený pro tvorbu
Clojure aplikací. Zároveň má jednu naprosto skvělou feature - žívá evaulace funkcí.
V začátcích nejvíc must have feature, kterou v začátcích chcete mít.

![Ligt Table](/img/posts/02-lighttable.png "Ligt Table")

## Leiningen

Možná je to balíčkovací systém, možná správce projektů, možná buildovací tool, … zkrátka
[Leiningen](http://leiningen.org/) za vás řeší celou řadu věcí okolo Clojure aplikace.
Jedná se o nepostradatelného pomocníka, kterého zkrátka musíte mít. Takže instalace je nutná.

## První aplikace

Máme připravené IDE a Leiningen. Pakliže máte nainstalované i [Clojure](http://clojure.org/downloads),
pak můžete hned začít.

```bash
$ lein new app demo-app
$ cd demo-app
```

Leiningen vytváří projekty na základě šablon. Na internetu jich najdete celou řadu.
Kupříkladu šablony pro tvorbu webových aplikací, apod.

## WTF

Tooling je sice cool, ale stále neřeší podivnost jazyka, který je pro nás stále jiný a hlavně divný.
Na úvod doporučuji přečíst článek [Clojure Tutorial for the Non-Lisp Programmer](http://www.moxleystratton.com/blog/2008/05/01/clojure-tutorial-for-the-non-lisp-programmer/).
Zkuste na to jít podobně. Vemte si třeba [Projekt Euler](https://projecteuler.net/archives) a
začněte řešit jednotlivé problémy. Nejprve to zkuste v jazyce, který znáte a poté to zkuste přepsat
do Clojure. Zjístíte, že to jde mnohdy zapsat téměř stejně.

### Příklad první

*If we list all the natural numbers below 10 that are multiples of 3 or 5, we get 3, 5, 6 *
*and 9. The sum of these multiples is 23. Find the sum of all the multiples of 3 or 5 below *
*1000.*

Relativně jednoduchý úkol - zkusme to nejprve v Javě.

```java
class ProjectEuler_01 {
  public static void main (String[] args) throws java.lang.Exception {
    int sum = 0;

    for (int number = 0; number < 1000; number++) {
      if (number % 3 == 0 || number % 5 == 0) {
        sum += number;
      }
    }
    System.out.println(sum);
  }
}
```

Jak by stejný kód vypadal v clojure?

```clojure
(reduce + (
  for [number (range 1 1000)
  :when (or
          (zero? (mod number 3))
          (zero? (mod number 5)))
  ] number))
```

Vypadá to téměř stejně. Jen cyklus for a podmínka. Proč je tedy Clojure lepší? Protože máme více funkcí!
Když poznáme další Clojure funkce tak zjistíme, že nemusíme vůbec použít cyklus for.

```clojure
(defn mod35 [number]
  (or
   (zero? (mod number 3))
   (zero? (mod number 5))))

(reduce + (filter mod35 (range 1 1000)))
```

Určitě se najdou i další řešení. Začínáte se do toho pomalu dostávat ...

## Funkce, funkce, funkce, funkce, ...

Krásná myšlenka. Mít minimum struktur, ale na druhou stranu mít maximum funkcí, které
nad těmito strukturama umí operovat.

A zde je právě největší problém Clojure, ale zároveň i největší síla. Kód je pro nás
hůře čitelný, neboť často neznáme použité funkce a nevíme co dělají. Scházejí
i jakákoliv klíčová slova, která by mohla zvýšit čitolnost, neboť přidávají kódu
určitý řád. Síla Clojure je v tom, že existuje mnoho, ale opravdu mnoho funkcí,
ze kterých náš kód následně komponujete. Krása. Řešíte skutečný problém.
Chvilku ale trvá, než poznáte sadu alespoň základních nejpoužívanějších funkcí.
Půjde to rychle, jakmile si osvojíte základní funkce a zvyknete si na struktury.

## Závorkové orgie

Když zmínite Clojure, tak buďte připravěni na vtípky okolo závorek. Podívejte se ale
znovu na příklad, který jsme před chvilkou společně vytvořili. Kontrolní otázka:
Když chceme zavolat jednu funkci, kde bude více závorkové omáčky?

```
() vs { (); }
```

## Kdy začnu myslet funkcionálně?

Další velmi častá otázka: "kdy si začal myslet funkcionálně?". Žádné velké osvícení nečekejte.
Celé je to jednodušší, než se na první pohled může zdát. Jak jsem psal výše - to kouzlo je ve
velkém počtu funkcí. Máte nějaký vstup a pomocí různých funkcí tento vstup transformujete.
Mnoho funkcí je už naimplementovaných a to velmi univerzálně. Nebudete mít příliš potřebu
psát vlástní věci. Časem si všimnete, že už ani nepotřebujete vytvářet nějaké abstrakce,
apod. Začne vám stačit vektor nebo jiné struktury. Nicméně už nebute mít nutkavou potřebu pro
všechnou vytvářet nové objekty. Všechno to jde tak nějak samo.

### Jak tedy asi vypadá takový myšlenkový pochod funkcionálního programátora?

1. Mám nějaká data a tohle s nimi chci udělat.
2. Tak to udělám.

Žádné přemýšlení nad factory, kontejnery, struktury, abstrakcemi, apod.

## Všechno jde, občas je jen potřeba se více zamyslet

Ve výsledku v Clojure zapíšete a vytvoříte vše, co například v Javě. A mnohdy to jde zapsat
i velmi podobným způsobem. Ale ještě častěji to lze zapsat jednodušeji Proto ten začátek nemusí
být tak těžký. Začnete poznávat nové funkce a začne se vám to stále více líbit. Je potřeba
si jen zvyknout na lehce odlišný zápis. Já jsem si například nikdy nedokázal představit webovou
aplikaci v Clojure - tento web jsem zkusil [přepsat do Clojure](https://github.com/jirkapenzes/blog)
a nebylo na tom nic příliš složitého a jiného. Existují knihovny, které za nás řeší routování, redering HTML
a další pilíře klasické webové aplikace. Po chvilce si dokážete představit i například kompletně immutabilní svět.

Mě Clojure doslova pohltilo. Píše se v tom krásně a rychle. Zkrátka doporučuji. A když nevíte jak něco
napsat, tak vždycky máte možnost napsat kód v Javě a poté to si z Clojure vesele zavolat přes JAR :)

Zkuste to také a pokud máte nějaký dotaz, tak mi neváhejte napsat. Dobré je, že vše, co se naučíte na
cestě za poznáním funkcionálního světa jistě oceníte i v OOP světě. Zajímavé a často odlišené způsoby
řešení problému a obecně dostanete zcela jiný pohled na věc. A to se hodí!

Tento článek z části navazuje na talk, který jsem měl v rámci únorového [TopMonks caffe](http://www.topmonks.com/caffe/).
Mimochodem skvělé akce, které se pořádají každý první čtvrtek v měsíci :)

<iframe src="https://docs.google.com/presentation/d/1RZ7-5So0TcN6A-1mLbshcX7kF-cJkhvtSFbFay33emI/embed?start=false&loop=false&delayms=3000"
  frameborder="0" width="480" height="299" allowfullscreen="true"
  mozallowfullscreen="true" webkitallowfullscreen="true">
</iframe>
