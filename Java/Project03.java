/*
 * Project03
 * 
 *   A practice with advanced ideas such as inheritance and the use of interfaces.
 *   These are data structures that provide a collection of songs and an ordering for how those songs will be played.
 *   
 *   @author Alvin Haryanto
 *   @version 20160928
 */

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.ArrayList;

public class Project03 {

	public static void main(String[] args) {
		Scanner in = new Scanner (System.in);
		SimplePlayList playlist = new SimplePlayList();
		getFile(in,playlist);
		printReport(in,playlist);
	}
	
	private static void getFile(Scanner in, SimplePlayList playlist1) {
		PlayListTrack track = new SimpleMusicTrack();
		System.out.print("Enter database filename: ");
		String inputFileName = in.nextLine();
		try {
			File file = new File (inputFileName);
			Scanner inputFile = new Scanner (file);
			while(track.getNextTrack(inputFile)){
				playlist1.addTrack(track);
				track = new SimpleMusicTrack();	
			}
			
			inputFile.close();}
		catch (IOException e) {
			System.out.println("There was a problem reading from "+inputFileName);}
	}
	
	private static void printReport(Scanner in,SimplePlayList playlist){
		System.out.println();
		System.out.println("Currently playing: No Song Playing");
		System.out.println("Next track to play: "+playlist.peekAtNextTrack());
		System.out.println("[P]lay next track");
		System.out.println("[A]dd a new track");
		System.out.println("[Q]uit");
		System.out.print("> ");
		String option=in.nextLine();
		
		while (!option.equals("q")){
			if (option.equals("p")){
				System.out.println();
				System.out.println("Currently playing: "+playlist.getNextTrack());
				if (!playlist.isEmpty()){
					System.out.println("Next track to play: "+playlist.peekAtNextTrack());
					System.out.println("[P]lay next track");
					System.out.println("[A]dd a new track");
					System.out.println("[Q]uit");
					System.out.print("> ");
					option=in.nextLine();
				}
				else{
					System.out.println("No tracks remaining");
					break;
				}
			}
			else {
				System.out.println();
				PlayListTrack newTrack = new SimpleMusicTrack();
				System.out.print("Track name: ");
				String newName=in.nextLine();
				newTrack.setName(newName);
				System.out.print("Artist name: ");
				String newArtist=in.nextLine();
				newTrack.setArtist(newArtist);
				System.out.print("Album name: ");
				String newAlbum=in.nextLine();
				newTrack.setAlbum(newAlbum);
				System.out.println();
				System.out.println("New track: "+newName);
				System.out.println("Artist: "+newArtist);
				System.out.println("Album: "+newAlbum);
				System.out.print("Are you sure you want to add this track [y/n]? ");
				if (in.nextLine().equals("y")){
					playlist.addTrack(newTrack);
				}
				System.out.println();
				System.out.println("Currently playing: "+playlist.getNextTrack());
				if (!playlist.isEmpty()){
					System.out.println("Next track to play: "+playlist.peekAtNextTrack());
					System.out.println("[P]lay next track");
					System.out.println("[A]dd a new track");
					System.out.println("[Q]uit");
					System.out.print("> ");
					option=in.nextLine();
				}
				else{
					System.out.println("No tracks remaining");
					break;
				}
			}
		}
		System.out.println();
		System.out.println("Tracks remaining in play list");
		System.out.println("------------------------------------------------------------");
		for (int i=1;!playlist.isEmpty();i++){
			System.out.println(i+" - "+playlist.getNextTrack());
		}
		
	}

}
