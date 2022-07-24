package com.project.fileTransfer;

import com.project.fileTransfer.Handler.Handler;
import com.project.fileTransfer.Handler.HandlerImpl;
import com.project.fileTransfer.constants.ApplicationConstants;
import com.project.fileTransfer.files.FileHandler;
import com.project.fileTransfer.files.FileHandlerImpl;
import com.project.fileTransfer.heartbeater.HeartBeaterImpl;
import com.project.fileTransfer.socket.UDPConnectorImpl;
import com.project.fileTransfer.socket.UdpConnector;
import com.project.fileTransfer.ui.FileSearchInterface;
import com.project.fileTransfer.ui.GUIController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Future;

import static com.project.fileTransfer.constants.ApplicationConstants.HEART_BEAT_RECEIVE_THRESHOLD;
import static com.project.fileTransfer.constants.ApplicationConstants.HEART_BEAT_SEND_THRESHOLD;
import static com.project.fileTransfer.constants.ApplicationConstants.isRegisterd;
import static com.project.fileTransfer.constants.ApplicationConstants.randomWithRange;

@SpringBootApplication
public class FileTransferApplication {


	public static void main(String[] args) throws InterruptedException {
//		ProgressBar pb = new ProgressBar("Registering in BS server||", 100);
//		pb.start();
//		pb.stepTo(35);
//		pb.stepTo(100);
//		pb.stop();


        System.out.println("This node operates in " + ApplicationConstants.IP + " and the port is " + ApplicationConstants.PORT);
		initLocalFileStorage();
		UdpConnector udpConnector = new UDPConnectorImpl();


		new Thread(() -> {
			while (true) {
				System.out.println(">>>>>>>>>> WAITING FOR REQUEST <<<<<<<<<<<<\n");
				Future<String> stringFuture = null;
				try {
					stringFuture = udpConnector.receive();
				} catch (IOException e) {
					e.printStackTrace();
				}
				while (!stringFuture.isDone()) {
				}

			}
		}
		).start();
		Thread.sleep(1000); // Wait until the system acknowledges the node


		/* For heart beating sending*/
		Runnable runnableHeartBeatSender = () -> {
			Handler handler = new HandlerImpl();
			Timer timer = new Timer();

			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					try {
						if (isRegisterd) {
							System.out.println("Sending Heart beat");
							handler.sendHeartBeatSignal();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, HEART_BEAT_SEND_THRESHOLD * 1000, HEART_BEAT_SEND_THRESHOLD * 1000);
		};
		Thread heartBeatSenderThread = new Thread(runnableHeartBeatSender);
		heartBeatSenderThread.start();

		/* For heart beating handling*/
		Runnable runnableHeartBeatHandler = () -> {
			HeartBeaterImpl heartBeater = HeartBeaterImpl.getInstance();
			Timer timer = new Timer();

			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					try {
						System.out.println("Handling  Heart beat");
						heartBeater.handleBeat();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, HEART_BEAT_RECEIVE_THRESHOLD * 1000, HEART_BEAT_RECEIVE_THRESHOLD * 1000);
		};
		Thread heartBeatHandlerThread = new Thread(runnableHeartBeatHandler);
		heartBeatHandlerThread.start();

        SpringApplication app = new SpringApplication(FileTransferApplication.class);
        app.setDefaultProperties(Collections
                .singletonMap("server.port",  ApplicationConstants.PORT));
        app.run(args);

	}

	private static void initLocalFileStorage() {
		FileHandler fileHandler = FileHandlerImpl.getInstance();
		String[] fullLocalFileArray = new String[]{
				"Adventures of Tintin",
				"Jack and Jill",
				"Glee",
				"The Vampire Diarie",
				"King Arthur",
				"Windows XP",
				"Harry Potter",
				"Kung Fu Panda",
				"Lady Gaga",
				"Twilight",
				"Windows 8",
				"Mission Impossible",
				"Turn Up The Music",
				"Super Mario",
				"American Pickers",
				"Microsoft Office 2010",
				"Happy Feet",
				"Modern Family",
				"American Idol",
				"Hacking for Dummies"
		};
		System.out.println("This node has :");
		ArrayList<String> fileList = new ArrayList<>();
		ArrayList<Integer> randomList = new ArrayList<>();
		int length = fullLocalFileArray.length;
		for (int i = 0; i < randomWithRange(3, 5); i++) {
			int random = randomWithRange(0, length - 1);
			boolean contains = randomList.contains(random);
			if (contains) {
				--i;
				continue;
			} else {
				randomList.add(random);
			}
			String s = fullLocalFileArray[random];
			System.out.println("\t" + s);
			String saltedName = s.replace(" ", "@");
			fileHandler.initializeFileStorage(saltedName);
			fileList.add(saltedName);
		}

		GUIController guiController = GUIController.getInstance();
		FileSearchInterface fileSearchInterface = new FileSearchInterface(guiController, fileList);
		guiController.setUIinstance(fileSearchInterface);
		fileSearchInterface.setVisible(true);

	}

}
