#!/usr/bin/env python3
import subprocess
import time

INTERFACE = 'end1'
SERVICE = 'photonvision'
UP_DELAY = 15  # seconds to wait after interface goes up
DOWN_DELAY = 4  # seconds interface must be down before stopping service

class FSM:
    WAITING = 'WAITING'
    ACTIVE = 'ACTIVE'

def interface_is_up(iface):
    try:
        result = subprocess.run(['cat', f'/sys/class/net/{iface}/operstate'], 
                                stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
        return result.stdout.strip() == 'up'
    except Exception as e:
        return False

def service_is_active(service):
    result = subprocess.run(['systemctl', 'is-active', service], stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
    return result.stdout.strip() == 'active'

def start_service(service):
    subprocess.run(['systemctl', 'start', service])

def stop_service(service):
    subprocess.run(['systemctl', 'stop', service])

def main():
    if service_is_active(SERVICE):
        state = FSM.ACTIVE
        print(f"[FSM] Detected {SERVICE} service is already active at startup.")
    else:
        state = FSM.WAITING
        print(f"[FSM] Detected {SERVICE} service is not active at startup.")

    down_since = None

    while True:
        up = interface_is_up(INTERFACE)

        if state == FSM.WAITING:
            if up:
                print(f"[FSM] Interface {INTERFACE} is up, waiting {UP_DELAY} seconds...")
                time.sleep(UP_DELAY)
                if interface_is_up(INTERFACE):
                    start_service(SERVICE)
                    print(f"[FSM] Started service {SERVICE}")
                    state = FSM.ACTIVE
            else:
                time.sleep(1)

        elif state == FSM.ACTIVE:
            if not up:
                if down_since is None:
                    down_since = time.time()
                    print(f"[FSM] Interface {INTERFACE} went down, timing...")
                elif time.time() - down_since >= DOWN_DELAY:
                    stop_service(SERVICE)
                    print(f"[FSM] Stopped service {SERVICE}, interface down {DOWN_DELAY}s")
                    down_since = None
                    state = FSM.WAITING
            else:
                down_since = None
            time.sleep(1)

if __name__ == '__main__':
    main()
