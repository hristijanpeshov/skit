package com.hvt.booking_lux.unitTesting;

import com.hvt.booking_lux.model.User;
import com.hvt.booking_lux.model.enumeration.Role;
import com.hvt.booking_lux.model.enumeration.Sentiment;
import com.hvt.booking_lux.model.statistics.CreatorYearStatistics;
import com.hvt.booking_lux.model.statistics.ResObjectYearStatistics;
import com.hvt.booking_lux.model.statistics.ReviewSentimentStatistics;
import com.hvt.booking_lux.repository.ReservationRepository;
import com.hvt.booking_lux.repository.UserRepository;
import com.hvt.booking_lux.service.UserStatisticsService;
import com.hvt.booking_lux.service.impl.UserStatisticsServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(MockitoJUnitRunner.class)
public class UserStatisticsServiceTests {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private UserRepository userRepository;
    private UserStatisticsService userStatisticsService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        userStatisticsService = Mockito.spy(new UserStatisticsServiceImpl(reservationRepository,userRepository));
    }

    @Test
    public void findAnnualPropertyReservationCountTest(){
        List<CreatorYearStatistics> list = new ArrayList<>();
        list.add(new CreatorYearStatistics() {
            @Override
            public int getMonth() {
                return 11;
            }

            @Override
            public int getNum() {
                return 20;
            }

            @Override
            public int getTotal() {
                return 20*35*7;
            }

            @Override
            public boolean equals(Object obj) {
                if(obj instanceof CreatorYearStatistics){
                    return getMonth()== ((CreatorYearStatistics) obj).getMonth()&&getNum()== ((CreatorYearStatistics) obj).getNum()&&getTotal()== ((CreatorYearStatistics) obj).getTotal();
                }
                return false;
            }
        });
        list.add(new CreatorYearStatistics() {
            @Override
            public int getMonth() {
                return 12;
            }

            @Override
            public int getNum() {
                return 10;
            }

            @Override
            public int getTotal() {
                return 10*35*7;
            }
            @Override
            public boolean equals(Object obj) {
                if(obj instanceof CreatorYearStatistics){
                    return getMonth()== ((CreatorYearStatistics) obj).getMonth()&&getNum()== ((CreatorYearStatistics) obj).getNum()&&getTotal()== ((CreatorYearStatistics) obj).getTotal();
                }
                return false;
            }
        });
        List<CreatorYearStatistics> list2 = new ArrayList<>();
        list2.add(new CreatorYearStatistics() {
            @Override
            public int getMonth() {
                return 11;
            }

            @Override
            public int getNum() {
                return 20;
            }

            @Override
            public int getTotal() {
                return 20*35*7;
            }
            @Override
            public boolean equals(Object obj) {
                if(obj instanceof CreatorYearStatistics){
                    return getMonth()== ((CreatorYearStatistics) obj).getMonth()&&getNum()== ((CreatorYearStatistics) obj).getNum()&&getTotal()== ((CreatorYearStatistics) obj).getTotal();
                }
                return false;
            }
        });
        list2.add(new CreatorYearStatistics() {
            @Override
            public int getMonth() {
                return 12;
            }

            @Override
            public int getNum() {
                return 10;
            }

            @Override
            public int getTotal() {
                return 10*35*7;
            }
            @Override
            public boolean equals(Object obj) {
                if(obj instanceof CreatorYearStatistics){
                    return getMonth()== ((CreatorYearStatistics) obj).getMonth()&&getNum()== ((CreatorYearStatistics) obj).getNum()&&getTotal()== ((CreatorYearStatistics) obj).getTotal();
                }
                return false;
            }
        });
        Mockito.when(reservationRepository.findAnnualPropertyReservationCount(Mockito.anyString(),Mockito.anyInt())).thenReturn(list);

        User user = new User("user@user.com","das","Das","Dsad", Role.ROLE_USER,"dsad","dasdas");

        Mockito.when(this.userRepository.findById("user@user.com")).thenReturn(java.util.Optional.of(user));

        for(int i=0;i<list.size();i++)
        {
            Assert.assertEquals(list2.get(i),userStatisticsService.findAnnualPropertyReservationCount(user,2000).get(i));
        }
    }

    @Test
    public void findAnnualReservationCountForPropertyTest(){
        List<ResObjectYearStatistics> list = new ArrayList<>();
        list.add(new ResObjectYearStatistics() {
            @Override
            public long getId() {
                return 1;
            }

            @Override
            public String getName() {
                return "name1";
            }

            @Override
            public int getMonth() {
                return 11;
            }

            @Override
            public int getNum() {
                return 20;
            }

            @Override
            public int getTotal() {
                return 20*35*7;
            }
            @Override
            public boolean equals(Object obj) {
                if(obj instanceof ResObjectYearStatistics){
                    return getId()== ((ResObjectYearStatistics) obj).getId()
                            && getNum()== ((ResObjectYearStatistics) obj).getNum()
                            && getMonth() == ((ResObjectYearStatistics) obj).getMonth()
                            && getTotal()== ((ResObjectYearStatistics) obj).getTotal()
                            && getName()== ((ResObjectYearStatistics) obj).getName();
                }
                return false;
            }
        });
        list.add(new ResObjectYearStatistics() {
            @Override
            public long getId() {
                return 1;
            }

            @Override
            public String getName() {
                return "name1";
            }

            @Override
            public int getMonth() {
                return 12;
            }

            @Override
            public int getNum() {
                return 10;
            }

            @Override
            public int getTotal() {
                return 20*35*7;
            }
            @Override
            public boolean equals(Object obj) {
                if(obj instanceof ResObjectYearStatistics){
                    return getId()== ((ResObjectYearStatistics) obj).getId()
                            && getNum()== ((ResObjectYearStatistics) obj).getNum()
                            && getMonth() == ((ResObjectYearStatistics) obj).getMonth()
                            && getTotal()== ((ResObjectYearStatistics) obj).getTotal()
                            && getName()== ((ResObjectYearStatistics) obj).getName();
                }
                return false;
            }
        });
        list.add(new ResObjectYearStatistics() {
            @Override
            public long getId() {
                return 1;
            }

            @Override
            public String getName() {
                return "name1";
            }

            @Override
            public int getMonth() {
                return 10;
            }

            @Override
            public int getNum() {
                return 35;
            }

            @Override
            public int getTotal() {
                return 35*35*7;
            }
            @Override
            public boolean equals(Object obj) {
                if(obj instanceof ResObjectYearStatistics){
                    return getId()== ((ResObjectYearStatistics) obj).getId()
                            && getNum()== ((ResObjectYearStatistics) obj).getNum()
                            && getMonth() == ((ResObjectYearStatistics) obj).getMonth()
                            && getTotal()== ((ResObjectYearStatistics) obj).getTotal()
                            && getName()== ((ResObjectYearStatistics) obj).getName();
                }
                return false;
            }
        });
        list.add(new ResObjectYearStatistics() {
            @Override
            public long getId() {
                return 1;
            }

            @Override
            public String getName() {
                return "name1";
            }

            @Override
            public int getMonth() {
                return 10;
            }

            @Override
            public int getNum() {
                return 30;
            }

            @Override
            public int getTotal() {
                return 30*35*7;
            }
            @Override
            public boolean equals(Object obj) {
                if(obj instanceof ResObjectYearStatistics){
                    return getId()== ((ResObjectYearStatistics) obj).getId()
                            && getNum()== ((ResObjectYearStatistics) obj).getNum()
                            && getMonth() == ((ResObjectYearStatistics) obj).getMonth()
                            && getTotal()== ((ResObjectYearStatistics) obj).getTotal()
                            && getName()== ((ResObjectYearStatistics) obj).getName();
                }
                return false;
            }
        });
        List<ResObjectYearStatistics> list2 = new ArrayList<>();
        list2.add(new ResObjectYearStatistics() {
            @Override
            public long getId() {
                return 1;
            }

            @Override
            public String getName() {
                return "name1";
            }

            @Override
            public int getMonth() {
                return 11;
            }

            @Override
            public int getNum() {
                return 20;
            }

            @Override
            public int getTotal() {
                return 20*35*7;
            }
            @Override
            public boolean equals(Object obj) {
                if(obj instanceof ResObjectYearStatistics){
                    return getId()== ((ResObjectYearStatistics) obj).getId()
                            && getNum()== ((ResObjectYearStatistics) obj).getNum()
                            && getMonth() == ((ResObjectYearStatistics) obj).getMonth()
                            && getTotal()== ((ResObjectYearStatistics) obj).getTotal()
                            && getName()== ((ResObjectYearStatistics) obj).getName();
                }
                return false;
            }
        });
        list2.add(new ResObjectYearStatistics() {
            @Override
            public long getId() {
                return 1;
            }

            @Override
            public String getName() {
                return "name1";
            }

            @Override
            public int getMonth() {
                return 12;
            }

            @Override
            public int getNum() {
                return 10;
            }

            @Override
            public int getTotal() {
                return 20*35*7;
            }
            @Override
            public boolean equals(Object obj) {
                if(obj instanceof ResObjectYearStatistics){
                    return getId()== ((ResObjectYearStatistics) obj).getId()
                            && getNum()== ((ResObjectYearStatistics) obj).getNum()
                            && getMonth() == ((ResObjectYearStatistics) obj).getMonth()
                            && getTotal()== ((ResObjectYearStatistics) obj).getTotal()
                            && getName()== ((ResObjectYearStatistics) obj).getName();
                }
                return false;
            }
        });
        list2.add(new ResObjectYearStatistics() {
            @Override
            public long getId() {
                return 1;
            }

            @Override
            public String getName() {
                return "name1";
            }

            @Override
            public int getMonth() {
                return 10;
            }

            @Override
            public int getNum() {
                return 35;
            }

            @Override
            public int getTotal() {
                return 35*35*7;
            }
            @Override
            public boolean equals(Object obj) {
                if(obj instanceof ResObjectYearStatistics){
                    return getId()== ((ResObjectYearStatistics) obj).getId()
                            && getNum()== ((ResObjectYearStatistics) obj).getNum()
                            && getMonth() == ((ResObjectYearStatistics) obj).getMonth()
                            && getTotal()== ((ResObjectYearStatistics) obj).getTotal()
                            && getName()== ((ResObjectYearStatistics) obj).getName();
                }
                return false;
            }
        });
        list2.add(new ResObjectYearStatistics() {
            @Override
            public long getId() {
                return 1;
            }

            @Override
            public String getName() {
                return "name1";
            }

            @Override
            public int getMonth() {
                return 10;
            }

            @Override
            public int getNum() {
                return 30;
            }

            @Override
            public int getTotal() {
                return 30*35*7;
            }
            @Override
            public boolean equals(Object obj) {
                if(obj instanceof ResObjectYearStatistics){
                    return getId()== ((ResObjectYearStatistics) obj).getId()
                            && getNum()== ((ResObjectYearStatistics) obj).getNum()
                            && getMonth() == ((ResObjectYearStatistics) obj).getMonth()
                            && getTotal()== ((ResObjectYearStatistics) obj).getTotal()
                            && getName()== ((ResObjectYearStatistics) obj).getName();
                }
                return false;
            }
        });
        Mockito.when(reservationRepository.findAnnualReservationCountForProperty(Mockito.anyString(), Mockito.anyInt(),Mockito.anyLong())).thenReturn(list);

        User user = new User("user@user.com","das","Das","Dsad", Role.ROLE_USER,"dsad","dasdas");

//        Mockito.when(this.userRepository.findById("user@user.com")).thenReturn(java.util.Optional.of(user));

        for(int i=0;i<list.size();i++)
        {
            Assert.assertEquals(list2.get(i),userStatisticsService.findAnnualReservationCountForProperty(user,2000,1).get(i));
        }
    }

    @Test
    public void findSentimentForResObjectTest(){
        List<ReviewSentimentStatistics> list = new ArrayList<>();
        list.add(new ReviewSentimentStatistics() {
            @Override
            public long getId() {
                return 1;
            }

            @Override
            public Sentiment getSentiment() {
                return Sentiment.POSITIVE;
            }
        });
        list.add(new ReviewSentimentStatistics() {
            @Override
            public long getId() {
                return 2;
            }

            @Override
            public Sentiment getSentiment() {
                return Sentiment.POSITIVE;
            }
        });
        list.add(new ReviewSentimentStatistics() {
            @Override
            public long getId() {
                return 3;
            }

            @Override
            public Sentiment getSentiment() {
                return Sentiment.NEGATIVE;
            }
        });

        Mockito.when(reservationRepository.findAllReviewsSentiment(Mockito.anyLong())).thenReturn(list);

        Map<String, Integer> map = new HashMap<>();
        map.putIfAbsent("POSITIVE", 2);
        map.putIfAbsent("NEGATIVE", 1);

        Map<String, Integer> result = userStatisticsService.findSentimentForResObject(0L);

        Assert.assertEquals(map,result);
    }

    @Test
    public void findZeroSentimentForResObjectTest(){
        List<ReviewSentimentStatistics> list = new ArrayList<>();

        Mockito.when(reservationRepository.findAllReviewsSentiment(1L)).thenReturn(list);

        Map<String, Integer> map = new HashMap<>();
        map.putIfAbsent("POSITIVE", 0);
        map.putIfAbsent("NEGATIVE", 0);

        Map<String, Integer> result = userStatisticsService.findSentimentForResObject(1L);

        Assert.assertEquals(map,result);
    }



}
