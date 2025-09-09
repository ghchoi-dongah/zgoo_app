document.addEventListener("DOMContentLoaded", function() {
    var places = [];

    /* map settings */
    var container = document.getElementById('map'); // 지도를 담을 영역의 DOM 레퍼런스
    var options = {                                 // 지도를 생성할 때 필요한 기본 옵션
        center: new kakao.maps.LatLng(37.480025, 126.878101), // 지도의 중심좌표
        level: 3                                    // 지도의 레벨(확대, 축소 정도)
    };

    var map = new kakao.maps.Map(container, options); // 지도 생성 및 객체 리턴
    var markers = []; 

    // 마커 클러스터 생성
    var clusterer = new kakao.maps.MarkerClusterer({
        map: map,
        averageCenter: true,
        minLevel: 10,
        disableClickZoom: true
    });

    // custom 마커이미지 정보
    var imgSrc = '../../img/marker.png',
        imgSize = new kakao.maps.Size(36, 50),
        imgOption = {offset: new kakao.maps.Point(27, 69)};

    // 마커의 이미지정보를 가지고 있는 마커이미지를 생성
    var markerImg = new kakao.maps.MarkerImage(imgSrc, imgSize, imgOption),
        markerPos = new kakao.maps.LatLng(37.480025, 126.878101);

    // 사용자 접속 위치를 초기 중심좌표로 세팅
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            var currLat = position.coords.latitude;
            var currLng = position.coords.longitude;
            var currPos = new kakao.maps.LatLng(currLat, currLng);

            var center = map.getCenter();
            removeMarkers();
            clusterer.clear();

            const url = `/map/nearby?latitude=${encodeURIComponent(center.getLat())}&longitude=${encodeURIComponent(center.getLng())}`;
            fetch(url, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then((response) => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status}`);
                    }
                    return response.json();
                })
                .then((data) => {
                    console.log('Nearby places:', data.csList);
                    places = placeList(data.csList);
                    nearbyStationList(data.csList);
                    mapMarkers();
                })
                .catch(error => {
                    console.log('Error:', error);
                });

            map.setCenter(currPos);
            map.setLevel(3);
        });
    }

    // 지도에 마커 표시 함수
    function mapMarkers() {
        for (var i = 0, len = places.length; i < len; i++) {
            (function(markerPosition) {
                // 마커를 생성하고 지도 위에 표시
                var marker = new kakao.maps.Marker({
                    map: map,
                    position: markerPosition.latlng,
                    image: markerImg
                });
        
                marker.stationId = markerPosition.stationId;
                markers.push(marker);

                // 클러스터리에 마커 추가
                clusterer.addMarkers(markers);
    
                // kakao.maps.event.addListener(marker, 'mouseover', makeOverListener(map, marker, infowindow));
                // kakao.maps.event.addListener(marker, 'mouseout', makeOutListener(infowindow));
                kakao.maps.event.addListener(marker, 'click', function() {
                    moveMapCenter(marker);
                    // searchStationDetail(marker.stationId);
                    // document.getElementById('stationInfoDetail').style.display = 'block';
                });
            })(places[i]);
        }
    }

    // 클러스터 클릭 시 현재 지도 레벨에서 1레벨 확대
    kakao.maps.event.addListener(clusterer, 'clusterclick', function(cluster) {
        var level = map.getLevel() - 1;
        map.setLevel(level, {anchor: cluster.getCenter()});
    });

    // 지도 중심좌표 변경 함수
    function moveMapCenter(marker) {
        var pos = marker.getPosition();
        map.panTo(pos);
    }

    // 사용자 주변 충전소
    function nearbyStationList(list) {

    }

    // 마커 삭제 함수
    function removeMarkers() {
        for (var i=0; i<markers.length; i++) {
            markers[i].setMap(null);    // 지도에서 마커 제거
        }
        markers = [];   // 초기화
    }

    // 지도에 출력할 장소 정보 리턴
    function placeList(csList) {
        var list = csList.map(function(cs) {
            return {
                stationId: cs.stationId,
                stationName: cs.stationName,
                latitude: cs.latitude,
                longitude: cs.longitude,
                latlng: new kakao.maps.LatLng(cs.latitude, cs.longitude)
            }
        });
        return list;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 검색
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    const searchContainer = document.getElementById("searchContainer");
    const stationEl = document.getElementById("STATION");
    const addressEl = document.getElementById("ADDRESS");
    const csSearch = document.getElementById("csSearch");

    searchContainer.addEventListener('click', function() {
        openAlert('searchSetting');
        const searchResult = document.getElementById('searchResult');
        csSearch.value = '';
        searchResult.innerHTML = '';
        searchResult.innerHTML = `<div class="csinfo-n">
                                    <i class="bi bi-search"></i>
                                    <span>검색 내역이 없습니다.</span>
                                </div>`;
        clickSearchOp("STATION");
    });

    function clickSearchOp(clickedId) {
        // 모든 대상 span의 text-focus 제거
        document.querySelectorAll('#STATION, #ADDRESS').forEach(el => {
            el.classList.remove('text-focus');
        });

        // 클릭된 span에만 text-focus 추가
        document.getElementById(clickedId).classList.add('text-focus');
    }

    // 이벤트 바인딩
    stationEl.addEventListener("click", function() {
        clickSearchOp("STATION");
        searchStation("STATION");
    });
    addressEl.addEventListener("click", function() {
        clickSearchOp("ADDRESS");
        searchStation("ADDRESS");
    });
    csSearch.addEventListener("input", function() {
        const focusedEl = document.querySelector("#STATION.text-focus, #ADDRESS.text-focus");

        if (focusedEl) {
            // console.log("현재 text-focus 요소:", focusedEl.id, focusedEl.textContent);
            searchStation(focusedEl.id);
        } else {
            console.log("없음");
        }

    });

    // 충전소 검색(충전소, 주소)
    function searchStation(option) {
        const csSearch = document.getElementById('csSearch').value;
        const searchResult = document.getElementById('searchResult');
        if (!csSearch || csSearch.trim() === "") {
            searchResult.innerHTML = '';
            searchResult.innerHTML = `<div class="csinfo-n">
                                        <i class="bi bi-search"></i>
                                        <span>검색 내역이 없습니다.</span>
                                    </div>`;
            return;
        }

        fetch(`/search/station?keyword=${encodeURIComponent(csSearch)}&option=${encodeURIComponent(option)}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`HTTP status: ${response.status}`);
                }
                return response.json();
            })
            .then((data) => {
                console.log('search station success');
                searchResult.innerHTML = '';
                
                if (!data.csList || data.csList.length === 0) {
                    searchResult.innerHTML = `<div class="csinfo-n">
                                        <i class="bi bi-search"></i>
                                        <span>검색 내역이 없습니다.</span>
                                    </div>`;
                    return;
                }

                let html = '';
                data.csList.forEach(cs => {
                    html += `
                        <div class="station-item csinfo-y" data-id=${cs.stationId}>
                            <div style="margin-bottom: 10px;">${cs.stationName}</div>
                            <div class="font-s">${cs.address}</div>
                        </div>`;
                });
                searchResult.innerHTML = html;

                document.querySelectorAll('.station-item').forEach(item => {
                    item.addEventListener('click', () => {
                        console.log(item.dataset.id);

                        fetch(`/find/station?stationId=${encodeURIComponent(item.dataset.id)}`, {
                            method: 'GET',
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        })
                            .then((response) => {
                                if (!response.ok) {
                                    throw new Error(`HTTP error! status: ${response.status}`);
                                }
                                return response.json();
                            })
                            .then((data) => {
                                if (!data.cs || data.cs.length === 0) {
                                    return;
                                }
                                document.getElementById('searchSetting').style.display = 'none';
                                // 지도 중심 좌표 변경
                                var pos = new kakao.maps.LatLng(data.cs.latitude, data.cs.longitude);
                                map.setCenter(pos);
                            })
                            .catch((error) => {
                                console.log('Error:', error);
                            });
                    });
                });
            })
            .catch((error) => {
                console.error('Error: ', error);
            });
    }
});