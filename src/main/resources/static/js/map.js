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
    
                kakao.maps.event.addListener(marker, 'click', function() {
                    moveMapCenter(marker);
                    searchStationDetail(marker.stationId);
                    window.StationDetail.expand();

                    // 1) 현재 선택된 스테이션 기록
                    const input = document.getElementById('stationId');
                    if (input) input.value = marker.stationId;

                    // 2) 캐시에 있는지 여부에 따라 아이콘 모양 갱신
                    updateStarUIByStationId(marker.stationId);
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

    function searchStationDetail(stationId) {
        fetch(`/find/station?stationId=${encodeURIComponent(stationId)}`, {
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
                console.log(data.cs);
                if (!data.cs || data.cs.length === 0) return;
                document.getElementById('stationName').innerHTML = data.cs.stationName;
                document.getElementById('stationId').value = data.cs.stationId;
                document.getElementById('address').innerHTML = data.cs.address;
                document.getElementById('addressDetail').innerHTML = data.cs.addressDetail;
                document.getElementById('opStatus').innerHTML = data.cs.opStatus;
                document.getElementById('opTime').innerHTML = `${data.cs.openStartTime} ~ ${data.cs.openEndTime}`;
                document.getElementById('parkingFee').innerHTML = data.cs.parkingFeeYn;
            })
            .catch((error) => {
                console.error('Error: ', error);
            });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 즐겨찾기
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ==== 즐겨찾기 캐시 유틸 ====
    const FAV_KEY = 'favStations'; // 로컬스토리지 키

    function getFavSet() {
        try {
            const arr = JSON.parse(localStorage.getItem(FAV_KEY) || '[]');
            return new Set(arr);
        } catch {
            return new Set();
        }
    }

    function saveFavSet(set) {
        localStorage.setItem(FAV_KEY, JSON.stringify([...set]));
    }

    function isFav(stationId) {
        const favs = getFavSet();
        return favs.has(String(stationId));
    }

    // ==== UI 토글 ====
    function setStarFilled(filled) {
        const star = document.getElementById('favStar');
        if (!star) return;
        star.classList.toggle('bi-star-fill', filled);
        star.classList.toggle('bi-star', !filled);
    }

    function updateStarUIByStationId(stationId) {
        setStarFilled(isFav(stationId));
    }

    // ==== 아이콘 클릭 핸들러 (토글) ====
    function toggleFavorite() {
        const input = document.getElementById('stationId');
        const stationId = input?.value;
        if (!stationId) return; // 현재 선택된 스테이션이 없으면 무시

        const favs = getFavSet();
        if (favs.has(stationId)) {
            favs.delete(stationId);
            saveFavSet(favs);
            setStarFilled(false);
        } else {
            favs.add(stationId);
            saveFavSet(favs);
            setStarFilled(true);
        }
    }

    // 아이콘 클릭 이벤트 연결
    document.getElementById('favStar')?.addEventListener('click', toggleFavorite);

    // 초기 진입 시(새로고침 등) stationId가 이미 세팅돼 있으면 UI 동기화
    document.addEventListener('DOMContentLoaded', () => {
        const cur = document.getElementById('stationId')?.value;
        if (cur) updateStarUIByStationId(cur);
    });

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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 충전소 상세 정보
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    const detail = document.getElementById('stationDetail');
    const handle = document.getElementById('slidebar');
    const content = detail.querySelector('.station-content');

    const MIN = 10;  // % (여기까지 내려오면 0%로 자동 접힘)
    const MAX = 50;  // %

    let dragging = false;
    let startY = 0;
    let startHpx = 0;
    let isCollapsing = false; // 애니메이션 중
    let isCollapsed = true;  // display:none 상태

    const vh = () => window.innerHeight;
    const clampPct = (pct) => Math.max(MIN, Math.min(MAX, pct));

    const disableTransition = () => detail.style.transition = 'none';
    const enableTransition  = () => detail.style.transition = 'height 0.2s ease';

    // 높이를 %로 세팅
    const setHeightPct = (pct) => { detail.style.height = pct + '%'; };

    // 완전 접기: 10% → 0% 트랜지션 후 display:none
    const collapseToNone = () => {
        if (isCollapsing || isCollapsed) return;
        isCollapsing = true;

        // 콘텐츠 투명화(선택)
        if (content) content.style.opacity = '0';

        // 현재 높이 강제 고정 후 0%로 트랜지션
        enableTransition();
        // 만약 현재가 MIN보다 작다면, 우선 MIN으로 맞춰 안정화
        const currentPct = (detail.offsetHeight / vh()) * 100;
        if (currentPct < MIN) setHeightPct(MIN);

        // 다음 프레임에서 0%로
        requestAnimationFrame(() => {
        setHeightPct(0);
        });

        const onEnd = (e) => {
        if (e.propertyName !== 'height') return;
        detail.removeEventListener('transitionend', onEnd);

        // 완전히 접혔으니 display:none 처리
        detail.style.display = 'none';
        isCollapsing = false;
        isCollapsed = true;
        };
        detail.addEventListener('transitionend', onEnd);
    };

    // 다시 펼치기: display:block → 0%에서 20%로 열리기
    const expandFromNone = () => {
        console.log('isCollapsed: ', isCollapsed);
        if (!isCollapsed) return;

        // 먼저 보이게
        detail.style.display = 'block';

        // 0%에서 시작하도록 세팅 (트랜지션 잠깐 끄고 height 초기화)
        disableTransition();
        setHeightPct(0);

        // 리플로우로 스타일 확정
        void detail.offsetHeight;

        // 트랜지션 켠 뒤 MIN%까지 펼치기
        enableTransition();
        requestAnimationFrame(() => {
            setHeightPct(20);
            if (content) content.style.opacity = '1';
        });

        isCollapsed = false;
    };

    // 드래그 시작
    const onDown = (e) => {
        if (isCollapsing) return;
        // 닫힌 상태에서 핸들이 클릭될 수 없으므로, 외부 버튼/제스처로 expand를 호출하세요.
        dragging = true;
        startY = (e.clientY ?? e.touches?.[0]?.clientY) || 0;
        startHpx = detail.offsetHeight;
        disableTransition();
        document.body.style.userSelect = 'none';

        if (handle.setPointerCapture && e.pointerId !== undefined) {
        handle.setPointerCapture(e.pointerId);
        }
    };

    // 드래그 중
    const onMove = (e) => {
        if (!dragging || isCollapsing) return;
        if (e.cancelable) e.preventDefault();

        const y = (e.clientY ?? e.touches?.[0]?.clientY) || 0;
        const deltaY = startY - y; // 위로 끌면 +
        let pct = ((startHpx + deltaY) / vh()) * 100;

        // MIN보다 작아졌다면 자동 접기 트리거
        if (pct <= MIN) {
            // 드래그 종료 처리를 먼저 해서 트랜지션 충돌 방지
            dragging = false;
            document.body.style.userSelect = '';
            collapseToNone();
            return;
        }

        // 범위 내에서만 변경
        pct = clampPct(pct);
        setHeightPct(pct);

        // 콘텐츠는 MIN 이상에서만 보이도록
        if (content) content.style.opacity = '1';
    };

    // 드래그 끝
    const onUp = () => {
        if (!dragging) return;
        dragging = false;
        enableTransition();
        document.body.style.userSelect = '';
    };

    // 이벤트 바인딩 (마우스/터치 겸용)
    handle.addEventListener('pointerdown', onDown);
    window.addEventListener('pointermove', onMove, { passive:false });
    window.addEventListener('pointerup', onUp);

    // 레거시 백업(옵션)
    handle.addEventListener('touchstart', onDown, { passive:false });
    window.addEventListener('touchmove', onMove, { passive:false });
    window.addEventListener('touchend', onUp);
    handle.addEventListener('mousedown', onDown);
    window.addEventListener('mousemove', onMove);
    window.addEventListener('mouseup', onUp);

    // (옵션) 외부 버튼으로 다시 펼치기
    document.getElementById('openDetail')?.addEventListener('click', expandFromNone);

    // 외부에서도 사용할 수 있게 노출 (옵션)
    window.StationDetail = {
        collapse: collapseToNone,
        expand: expandFromNone
    };
});