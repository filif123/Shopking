package sk.shopking.tools;

import java.util.ArrayList;
/**
 * @author Filip
 * @deprecated
 *
 */
public class Barcode {
	private long code;
	private String country;
	private String type;
	private int manufacturer;
	private int codeVyrobok;
	//TODO nekontroluje spravnost kodu
	private boolean correctlyCode = true;
	
	public Barcode(String sBarcode){
		this.code = Long.parseLong(sBarcode);
		this.type = findOutTypeCode(ShopKingTools.longToLongArray(this.code));
		this.country = findOutCountry(ShopKingTools.longToLongArray(this.code));
		if(this.type == "EAN13") {
			findOutManufacturerEAN13(ShopKingTools.longToLongArray(this.code));
		}
		
	}
	
	public Barcode(long iBarcode){
		this.code = iBarcode;
		this.type = findOutTypeCode(ShopKingTools.longToLongArray(this.code));
		this.country = findOutCountry(ShopKingTools.longToLongArray(this.code));
		if(this.type == "EAN13") {
			findOutManufacturerEAN13(ShopKingTools.longToLongArray(this.code));
		}
	}
	
	public String findOutTypeCode(ArrayList<Long> aBarcode) {
		if(aBarcode.size() == 13) {
			return "EAN13";
		}
		else if(aBarcode.size() == 8) {
			return "EAN8";
		}
		else{
			return null;
		}
	}
	
	public int findOutManufacturerEAN13(ArrayList<Long> aBarcode) {
		ArrayList<Long> manufacturerArray = new ArrayList<Long>();
		manufacturerArray.add(0,aBarcode.get(3));
		manufacturerArray.add(1,aBarcode.get(4));
		manufacturerArray.add(2,aBarcode.get(5));
		manufacturerArray.add(3,aBarcode.get(6));
		manufacturerArray.add(4,aBarcode.get(7));
		return ShopKingTools.longArrayToLong(manufacturerArray);
		
	}
	
	public int findOutCodeVyrobokEAN13(ArrayList<Long> aBarcode) {
		ArrayList<Long> vyrobokArray = new ArrayList<Long>();
		vyrobokArray.add(0,aBarcode.get(8));
		vyrobokArray.add(1,aBarcode.get(9));
		vyrobokArray.add(2,aBarcode.get(10));
		vyrobokArray.add(3,aBarcode.get(11));
		vyrobokArray.add(4,aBarcode.get(12));
		return ShopKingTools.longArrayToLong(vyrobokArray);
	}
	
	public String findOutCountry(ArrayList<Long> aBarcode) {
		ArrayList<Long> countryArray = new ArrayList<Long>();
		countryArray.add(0,aBarcode.get(0));
		countryArray.add(1,aBarcode.get(1));
		countryArray.add(2,aBarcode.get(2));
		int iCountry = ShopKingTools.longArrayToLong(countryArray);
		
		if(iCountry >= 0 && iCountry <= 19) {
			return "USA, Kanada";
		}
		else if(iCountry >= 20 && iCountry <= 29) {
			return "LOCAL";
		}
		else if(iCountry >= 30 && iCountry <= 39) {
			return "USA,Kanada Lieky";
		}
		else if(iCountry >= 40 && iCountry <= 49) {
			return "LOCAL";
		}
		else if(iCountry >= 50 && iCountry <= 59) {
			return "poukazky, sazenky";
		}
		else if(iCountry >= 60 && iCountry <= 99) {
			return "USA, Kanada";
		}
		else if(iCountry >= 100 && iCountry <= 139) {
			return "USA, Kanada";
		}
		else if(iCountry >= 200 && iCountry <= 299) {
			return "LOCAL";
		}
		else if(iCountry >= 400 && iCountry <= 440) {
			return "Germany";
		}
		else if(iCountry >= 450 && iCountry <= 459) {
			return "Japan";
		}
		else if(iCountry >= 460 && iCountry <= 469) {
			return "Russia";
		}
		else if(iCountry >= 490 && iCountry <= 499) {
			return "Japan";
		}
		else if(iCountry >= 500 && iCountry <= 509) {
			return "UK";
		}
		else if(iCountry >= 540 && iCountry <= 549) {
			return "Belgie, Luxembourg";
		}
		else if(iCountry >= 570 && iCountry <= 579) {
			return "Dansko";
		}
		else if(iCountry >= 600 && iCountry <=601) {
			return "Juhoafricka rep.";
		}
		else if(iCountry >= 640 && iCountry <= 649) {
			return "Finland";
		}
		else if(iCountry >= 690 && iCountry <= 699) {
			return "China";
		}
		else if(iCountry >= 700 && iCountry <= 709) {
			return "Norsko";
		}
		else if(iCountry >= 730 && iCountry <= 739) {
			return "Svedsko";
		}
		else if(iCountry >= 754 && iCountry <= 755) {
			return "Kanada";
		}
		else if(iCountry >= 760 && iCountry <= 769) {
			return "Svajciarsko";
		}
		else if(iCountry >= 789 && iCountry <= 790) {
			return "Brazil";
		}
		else if(iCountry >= 800 && iCountry <= 839) {
			return "Italia";
		}
		else if(iCountry >= 840 && iCountry <= 849) {
			return "Spain";
		}
		else if(iCountry >= 870 && iCountry <= 879) {
			return "Holandsko";
		}
		else if(iCountry >= 900 && iCountry <= 919) {
			return "Austria";
		}
		else if(iCountry >= 930 && iCountry <= 939) {
			return "Australia";
		}
		else if(iCountry >= 940 && iCountry <= 949) {
			return "New Zealand";
		}
		else if(iCountry >= 978 && iCountry <= 979) {
			return "ISBN";
		}
		else if(iCountry >= 981 && iCountry <= 982) {
			return "Bezne platobne pokazky";
		}
		else if(iCountry >= 990 && iCountry <= 999) {
			return "Poukazky";
		}
		else {
			switch(iCountry) {
			case 380:
				return "Bulgaria";
			case 383:
				return "Slovenia";
			case 385:
				return "Croatia";	
			case 387:
				return "Bosna";
			case 470:
				return "Kyrgyzstán";
			case 471:
				return "cina";
			case 474:
				return "Estonia";
			case 475:
				return "Lotyssko";
			case 476:
				return "Azerbájdžán";
			case 477:
				return "Litva";
			case 478:
				return "Uzbekistan";
			case 479:
				return "Sri Lanka";
			case 480:
				return "Filipiny";
			case 481:
				return "Belarus";
			case 482:
				return "Ukraine";
			case 484:
				return "Moldavia";
			case 485:
				return "Armenia";
			case 486:
				return "Gruzinsko";
			case 487:
				return "Kazacstan";
			case 489:
				return "Hongkong";
			case 520:
				return "Grece";
			case 528:
				return "Libanon";
			case 529:
				return "Cyprus";
			case 531:
				return "Macedonsko";
			case 535:
				return "Malta";
			case 539:
				return "Irsko";
			case 560:
				return "Portugal";
			case 569:
				return "Island";
			case 590:
				return "Poland";
			case 594:
				return "Rumunia";
			case 599:
				return "Hungary";
			case 608:
				return "Bahrajn";
			case 609:
				return "Mauricius";
			case 611:
				return "Maroko";
			case 613:
				return "Alzirsko";
			case 616:
				return "Kena";
			case 619:
				return "Tunisko";
			case 621:
				return "Siria";
			case 622:
				return "Egypt";
			case 624:
				return "Lybia";
			case 625:
				return "Jordansko";
			case 626:
				return "Iran";
			case 627:
				return "Kuvajt";
			case 628:
				return "Saudi Araba";
			case 629:
				return "Sp. arab emiraty";
			case 729:
				return "Izrael";
			case 740:
				return "Guatemala";
			case 741:
				return "Salvador";
			case 742:
				return "Honduras";
			case 743:
				return "Nikaragua";
			case 744:
				return "Kostarika";
			case 745:
				return "Panama";
			case 746:
				return "Dominikanska republika";
			case 750:
				return "Mexiko";
			case 759:
				return "Venezuela";
			case 770:
				return "Kolumbia";
			case 773:
				return "Uruguay";
			case 775:
				return "Peru";
			case 777:
				return "Bolivia";
			case 779:
				return "Argentina";
			case 780:
				return "Chile";
			case 784:
				return "Paraguay";
			case 785:
				return "Peru";
			case 786:
				return "Ekvador";
			case 850:
				return "Kuba";
			case 858:
				return "Slovakia";
			case 859:
				return "Cesko";
			case 860:
				return "Srbsko, CirnaHora";
			case 865:
				return "Mongolsko";
			case 867:
				return "Severna Korea";
			case 869:
				return "Turecko";
			case 880:
				return "Juzna Korea";
			case 884:
				return "Kambodza";
			case 885:
				return "Thajsko";
			case 888:
				return "Singapur";
			case 890:
				return "India";
			case 893:
				return "Vietnam";
			case 899:
				return "Indonezia";
			case 950:
				return "Centrala";
			case 955:
				return "Malajzia";
			case 958:
				return "Macao";
			case 977:
				return "ISSN";
			case 980:
				return "Vratne uctenky";
			default:
				return null;	
			}
		}
	}

	public String getCountry() {
		return country;
	}

	public int getManufacturer() {
		return manufacturer;
	}

	public long getCode() {
		return codeVyrobok;
	}

	public boolean isCorrectlyCode() {
		return correctlyCode;
	}
	
	
}
