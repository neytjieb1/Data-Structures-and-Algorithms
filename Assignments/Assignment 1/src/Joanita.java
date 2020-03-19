public class Joanita {
    public Item[] rootMonth = new Item[12];
    public Item[] rootDay = new Item[31];

    public void addItemt(int day, String month, String description, String duration, int priority) {
        Item add = new Item(description, duration, priority, day, month.toLowerCase());
        int d = day - 1;
        int m = add.getMonth();
        if (rootMonth[m] == null) {//kolom (maand) is leeg
            add.down = null;

            //case 1:
            //eenvoudig, die day en die month is leeg
            if (rootDay[d] == null) {
                add.right = null;
                add.back = null;
                rootMonth[m] = add;
                rootDay[d] = rootMonth[m];
				/*System.out.println(Months[m]);
				System.out.println(Days[d]);
				System.out.println(add);*/
                return;
            }//---------------------------------------
            //---------------------------------------
            //tot hier werk dit 100%


            //case 2:
            //Days[d] het een element
            if (rootDay[d] != null && rootDay[d].right == null) {
                //m is groter as die eerste item van Days[d].month so hy moet agter Days[d] kom
                if (rootDay[d].month < m) {
                    add.right = null;
                    add.back = null;
                    rootDay[d].right = add;
                    rootMonth[m] = rootDay[d].right;
                    return;
                }//-----------------------------------------------------------------------------
                //werk

                //m is kleiner as die eerste item in Days[d] so hy moet voor kom
                if (rootDay[d].month > m) {
                    add.right = rootDay[d];
                    add.back = null;
                    rootDay[d] = add;
                    rootMonth[m] = rootDay[d];
                    return;
                }//---------------------------------------------------------------
                //werk
            }
            //-----------------------
            //-----------------------
            //tot hier werk dit 100%


            //case 3
            //Days[d] het meer as een maand se events al in.
            //as hy net een item gehad het sal die vorige if execute
            if (rootDay[d] != null) {
                //System.out.println("hieeer");
                Item nodeDay = rootDay[d];
                //sit voor in die ry in
                if (rootDay[d].month > m) {
                    add.right = nodeDay;
                    add.back = null;
                    rootDay[d] = add;
                    rootMonth[m] = rootDay[d];//ekstra
                    return;
                }//werk
                //----------------------------

                //is die eerste element las agter by
                if (rootDay[d].month == m) {
                    Item nodePtr = rootDay[d];
                    Item prev = null;
                    //moet voor die huidige item in kom
                    if (nodePtr.getPriority() < add.getPriority()) {
                        add.right = null;
                        //insert gedeelte wat met prioriteit werk
                        nodePtr.back = null;
                        add.back = nodePtr;
                        add.right = nodePtr.right;
                        add.down = nodePtr.down;
                        rootDay[d] = add;
                        rootMonth[m] = rootDay[d];
                        return;
                        //------------------------------
                        // werk

                    } else {
                        //moet agter die huidige element ingesit word
                        add.right = null;
                        //insert gedeelte wat met prioriteit werk
                        while (nodePtr != null) {

                            if (nodePtr.getPriority() < add.getPriority()) {
                                add.back = nodePtr;
                                prev.back = add;
                                return;
                            }
                            if (nodePtr.getPriority() == add.getPriority()) {
                                add.back = nodePtr.back;
                                nodePtr.back = add;
                                return;
                            }
                            if (nodePtr.getPriority() > add.getPriority()) {
                                prev = nodePtr;
                                nodePtr = nodePtr.back;
                            }
                        }
                        prev.back = add;
                        return;
                    }//werk
                }//werk
                //------------------------------------------------------

                //moet na die eerste node insit
                //System.out.println(m);
                if (rootDay[d].month < m) {
                    //	System.out.println("kom hier in");
                    Item prev = null;
                    while (nodeDay != null) {
                        if (nodeDay.month > m) {
                            add.right = nodeDay;
                            add.back = null;
                            prev.right = add;
                            rootMonth[m] = prev.right;
                            return;
                        }//werk

                        if (nodeDay.month == m) {
                            Item nodePtr = nodeDay;
                            Item place = new Item();
                            place.right = nodeDay.right;
                            place.down = nodeDay.down;
                            //System.out.println(place);
                            Item terug = null;
                            //voor die huidige element
                            if (nodePtr.getPriority() < add.getPriority()) {
                                add.right = nodeDay.right;
                                add.back = nodePtr;
                                place = add;
                                rootMonth[m] = place;
                                prev.right = place;
                                return;
                            } else {//agter die huidige element
                                add.right = null;
                                while (nodePtr != null) {
                                    if (nodePtr.getPriority() == add.getPriority()) {
                                        add.back = nodePtr.back;
                                        nodePtr.back = add;
                                        return;
                                    }
                                    if (nodePtr.getPriority() > add.getPriority()) {
                                        terug = nodePtr;
                                        nodePtr = nodePtr.back;
                                        continue;
                                    }
                                    if (nodePtr.getPriority() < add.getPriority()) {
                                        add.back = nodePtr;
                                        terug.back = add;
                                        return;
                                    }
                                }
                                add.back = null;
                                terug.back = add;
                                return;
                            }

                        }//------- Days[d].month==m

                        if (nodeDay.month < m) {
                            prev = nodeDay;
                            nodeDay = nodeDay.right;
                        }//werk
                    }
                    //sit die maand in
                    prev.right = add;
                    rootMonth[m] = prev.right;
					/*System.out.println(prev.getDescription());
					System.out.println(prev.back.getDescription());*/
					/*System.out.println(prev.back);
					System.out.println(Months[m]);*/
                    return;
                }//------------------------------------
                //------------------------------------
            }
            //----------------------------------------------
            //----------------------------------------------case3

        }//kolom (maand) is nie leeg nie
        else {
            //sit n item in as daar net 1 item is, selfde dag en maand
            if (rootMonth[m] != null && rootMonth[m].down == null && rootMonth[m].day == d) {
                //System.out.println("hieeeeeeeeer");
                //Days[d].month = month, werk met priority
                if (rootDay[d].month == m) {
                    Item nodePtr = rootDay[d];
                    Item prev = null;

                    //moet voor die huidige item in kom
                    if (nodePtr.getPriority() < add.getPriority()) {
                        add.right = null;
                        //insert gedeelte wat met prioriteit werk
                        nodePtr.back = null;
                        add.back = nodePtr;
                        add.right = nodePtr.right;
                        add.down = nodePtr.down;
                        rootDay[d] = add;
                        rootMonth[m] = rootDay[d];
						/*System.out.println(Days[d]);
						System.out.println(Months[m]);
						System.out.println(add);*/
                        return;
                        //------------------------------
                        // werk

                    } else {
                        //moet agter die huidige element ingesit word
                        add.right = null;
                        //insert gedeelte wat met prioriteit werk
                        while (nodePtr != null) {

                            if (nodePtr.getPriority() < add.getPriority()) {
                                add.back = nodePtr;
                                prev.back = add;
                                return;
                            }
                            if (nodePtr.getPriority() == add.getPriority()) {
                                add.back = nodePtr.back;
                                nodePtr.back = add;
                                return;
                            }
                            if (nodePtr.getPriority() > add.getPriority()) {
                                prev = nodePtr;
                                nodePtr = nodePtr.back;
                            }
                        }
                        prev.back = add;
                        return;
                    }//------------------------------
                    // werk

                }
                if (rootDay[d] != null) {
                    //System.out.println("hieeer");
                    Item nodeDay = rootDay[d];
                    //sit voor in die ry in
                    if (rootDay[d].month > m) {
                        add.right = nodeDay;
                        add.back = null;
                        rootDay[d] = add;
                        rootMonth[m] = rootDay[d];//ekstra
					/*System.out.println(Days[d]);
					System.out.println(Months[m]);*/
                        return;
                    }//werk
                    //----------------------------

                    //is die eerste element las agter by
                    if (rootDay[d].month == m) {
                        Item nodePtr = rootDay[d];
                        Item prev = null;
                        //moet voor die huidige item in kom
                        if (nodePtr.getPriority() < add.getPriority()) {
                            add.right = null;
                            //insert gedeelte wat met prioriteit werk
                            nodePtr.back = null;
                            add.back = nodePtr;
                            add.right = nodePtr.right;
                            add.down = nodePtr.down;
                            rootDay[d] = add;
                            rootMonth[m] = rootDay[d];
						/*System.out.println(Days[d]);
						System.out.println(Months[m]);
						System.out.println(add);*/
                            return;
                            //------------------------------
                            // werk

                        } else {
                            //moet agter die huidige element ingesit word
                            add.right = null;
                            //insert gedeelte wat met prioriteit werk
                            while (nodePtr != null) {

                                if (nodePtr.getPriority() < add.getPriority()) {
                                    add.back = nodePtr;
                                    prev.back = add;
                                    return;
                                }
                                if (nodePtr.getPriority() == add.getPriority()) {
                                    add.back = nodePtr.back;
                                    nodePtr.back = add;
                                    return;
                                }
                                if (nodePtr.getPriority() > add.getPriority()) {
                                    prev = nodePtr;
                                    nodePtr = nodePtr.back;
                                }
                            }
                            prev.back = add;
                            return;
                        }//werk
                    }//werk
                    //------------------------------------------------------

                    //moet na die eerste node insit
                    //System.out.println(m);
                    if (rootDay[d].month < m) {
                        //	System.out.println("kom hier in");
                        Item prev = null;
                        while (nodeDay != null) {
                            if (nodeDay.month > m) {
                                add.right = nodeDay;
                                add.back = null;
                                prev.right = add;
                                rootMonth[m] = prev.right;
                                return;
                            }//werk

                            if (nodeDay.month == m) {
                                Item nodePtr = nodeDay;
                                Item place = new Item();
                                place.right = nodeDay.right;
                                place.down = nodeDay.down;
                                //System.out.println(place);
                                Item terug = null;
                                //voor die huidige element
                                if (nodePtr.getPriority() < add.getPriority()) {
                                    add.right = nodeDay.right;
                                    add.back = nodePtr;
                                    place = add;
                                    rootMonth[m] = place;
                                    prev.right = place;
                                    return;
                                } else {//agter die huidige element
                                    add.right = null;
                                    while (nodePtr != null) {
                                        if (nodePtr.getPriority() == add.getPriority()) {
                                            add.back = nodePtr.back;
                                            nodePtr.back = add;
                                            return;
                                        }
                                        if (nodePtr.getPriority() > add.getPriority()) {
                                            terug = nodePtr;
                                            nodePtr = nodePtr.back;
                                            continue;
                                        }
                                        if (nodePtr.getPriority() < add.getPriority()) {
                                            add.back = nodePtr;
                                            terug.back = add;
                                            return;
                                        }
                                    }
                                    add.back = null;
                                    terug.back = add;
                                    return;
                                }

                            }//------- Days[d].month==m

                            if (nodeDay.month < m) {
                                prev = nodeDay;
                                nodeDay = nodeDay.right;
                            }//werk
                        }
                        //sit die maand in
                        prev.right = add;
                        rootMonth[m] = prev.right;
                        return;
                    }//------------------------------------
                    //------------------------------------


                }
                //-----------------------------
                //-----------------------------
            } else {//meer as een item


                if (rootMonth[m].day > d) {//sit voor die huidge element is
                    //hoef nie oor priorities te worry nie
                    //System.out.println("hier");
                    if (rootDay[d] == null) {
                        //System.out.println("hier");
                        add.down = rootMonth[m];
                        add.right = null;
                        add.back = null;
                        rootMonth[m] = add;
                        rootDay[d] = rootMonth[m];
                        return;
                    } else {//het reeds n dag in
                        //begin hier werk!! priorities
                        //System.out.println("hier");
                        Item nodePtr = rootDay[d];//nodePtr is die ptr wat die dae toets
                        Item prev = null;
							/*System.out.println(Days[d].month);
							System.out.println(Days[d].right.month);*/
							/*while(nodePtr!=null){
								System.out.println(nodePtr.month);
								nodePtr= nodePtr.right;
							}
							System.out.println();*/
                        //System.out.println(Days[d].month);

                        while (nodePtr != null) {
                            //System.out.println(nodePtr.month);
                            //System.out.println("hier");
                            if (nodePtr.month > m) {//las voor aan
                                //System.out.println("hier");
                                add.right = nodePtr;
                                add.down = rootMonth[m];
                                add.back = null;
                                if (nodePtr == rootDay[d]) {
                                    //System.out.println("hier");
                                    rootDay[d] = add;
                                    rootMonth[m] = rootDay[d];
                                    return;
                                } else {
                                    prev.right = add;
                                    rootMonth[m] = prev.right;
                                    return;
                                }
                            }
                            //die comment is nodePtr.month==m
								/*if(nodePtr.month==m){
									//System.out.println("hier");
									if (nodePtr.getPriority() < add.getPriority()){
										add.back= nodePtr;
										add.right= nodePtr.right;
										add.down= nodePtr.down;
										if(nodePtr==Days[d]) {
											Days[d] = add;
											Months[m] = Days[d];
											return;
										}
										prev.right= add;
										Months[m]= prev.right;
										return;
									} else {
										Item move = nodePtr;
										Item terug = null;

										while (move != null) {
											if (move.getPriority() < add.getPriority()) {
												add.back = move;
												terug.back = add;
												return;
											}
											if (move.getPriority() == add.getPriority()) {
												add.back = move.back;
												move.back = add;
												return;
											}
											if (move.getPriority() > add.getPriority()) {
												terug = move;
												move = move.back;
											}
										}
										terug.back = add;
										return;
									}
								}//nodePtr.month==m*/

                            if (nodePtr.month < m) {
                                //	System.out.println("hier");
                                prev = nodePtr;
                                nodePtr = nodePtr.right;
                            }
                        }//while
                        //hier kom goed
                        prev.right = add;
                        return;

                    }
                }
                if (rootMonth[m].day == d) {
                    //	System.out.println("hierrrr");////////werk hier nou
                    if (rootDay[d] != null && rootDay[d].right == null) {
                        if (rootMonth[m].getPriority() < add.getPriority()) {
                            add.right = rootMonth[m].right;
                            add.down = rootMonth[m].down;
                            add.back = rootMonth[m];
                            rootMonth[m] = add;
                            rootDay[d] = rootMonth[m];
                            return;
                        } else {
                            //moet agter die huidige prioriteit kom
                            add.right = null;
                            Item prev = null;
                            Item nodePtr = rootMonth[m];
                            while (nodePtr != null) {
                                if (nodePtr.getPriority() < add.getPriority()) {
                                    add.back = nodePtr;
                                    prev.back = add;
                                    return;
                                }
                                if (nodePtr.getPriority() == add.getPriority()) {
                                    add.back = nodePtr.back;
                                    nodePtr.back = add;
                                    return;
                                }
                                if (nodePtr.getPriority() > add.getPriority()) {
                                    prev = nodePtr;
                                    nodePtr = nodePtr.back;
                                }
                            }
                            prev.back = add;
                            return;
                        }
                    } else {
                        //System.out.println("hierrr");
                        //moet nof doen
                        //soek die posisie van die dag
                        //System.out.println(Days[d].month);
                        Item nodePtr = rootDay[d];
                        Item prev = null;
                        while (nodePtr != null) {
                            if (nodePtr.month < m) {
                                prev = nodePtr;
                                nodePtr = nodePtr.right;
                                continue;
                            }
                            if (nodePtr.month == m) {
                                //System.out.println("op die regte plek");
                                if (rootMonth[m].getPriority() < add.getPriority()) {
                                    //System.out.println("regte plek");
                                    add.right = rootMonth[m].right;
                                    add.down = rootMonth[m].down;
                                    add.back = rootMonth[m];
                                    rootMonth[m] = add;
                                    if (nodePtr == rootDay[d]) {
                                        rootDay[d] = rootMonth[m];
                                        return;
                                    }
                                    prev.right = rootMonth[m];
                                    return;
                                } else {
                                    add.right = null;
                                    add.down = null;
                                    Item terug = null;
                                    Item move = rootMonth[m];
                                    while (move != null) {
                                        if (move.getPriority() < add.getPriority()) {
                                            add.back = move;
                                            terug.back = add;
                                            return;
                                        }
                                        if (move.getPriority() == add.getPriority()) {
                                            add.back = move.back;
                                            move.back = add;
                                            return;
                                        }
                                        if (move.getPriority() > add.getPriority()) {
                                            terug = move;
                                            move = move.back;
                                        }
                                    }
                                    terug.back = add;
                                    return;
                                }

                            }
                        }//while
                        //dink nie hier hoef iets te wees nie
                    }
                }
                if (rootMonth[m].day < d) {//moet traverse en na die regte plek toe gaan
                    //	System.out.println("hier");
                    Item nodePtr = rootMonth[m];
                    Item prev = null;
                    if (rootDay[d] == null || (rootDay[d] != null && rootDay[d].right == null && rootDay[d].month == m)) {
                        //	System.out.println("hierrrr");
                        while (nodePtr != null) {
                            //System.out.println("hieeer");
                            if (nodePtr.day == d) {
								/*Item move = nodePtr;
								Item terug = null;
								while(move!= null) {
									if (nodePtr.getPriority() < add.getPriority()) {
										add.down = move.down;
										add.right = null;
										add.back = nodePtr;
										Days[d] = add;
										nodePtr= Days[d];
										return;
									}else{

									}*/
								/*if (move.getPriority() == add.getPriority()) {
										add.back = nodePtr.back;
										move.back = add;
										return;
									}
									if(move.getPriority()>add.getPriority()){
										terug= move;
										move = move.back;
									}*/
                                if (nodePtr.getPriority() < add.getPriority()) {
                                    add.right = nodePtr.right;
                                    add.back = nodePtr;
                                    add.down = nodePtr.down;
                                    if (prev != null) {
                                        prev.down = add;
                                        rootDay[d] = prev.down;
                                        return;
                                    }
                                    rootMonth[m] = add;
                                    rootDay[d] = rootMonth[m];
                                    return;
                                } else {
                                    Item move = nodePtr;
                                    Item terug = null;
                                    while (move != null) {
                                        if (move.getPriority() < add.getPriority()) {
                                            add.back = move;
                                            terug.back = add;
                                            return;
                                        }
                                        if (move.getPriority() == add.getPriority()) {
                                            add.back = move.back;
                                            move.back = add;
                                            return;
                                        }
                                        if (move.getPriority() > add.getPriority()) {
                                            terug = move;
                                            move = move.back;
                                        }
                                    }
                                    terug.back = add;
                                    return;
                                }


                            }
                            if (nodePtr.day < d) {//traverse
                                prev = nodePtr;
                                nodePtr = nodePtr.down;
                                continue;
                            }
                            if (nodePtr.day > d) {
                                add.down = nodePtr;
                                add.right = null;
                                add.back = null;
                                prev.down = add;
                                rootDay[d] = prev.down;
                                return;
                            }
                        }
                        add.down = null;
                        add.right = null;
                        add.back = null;
                        prev.down = add;
                        rootDay[d] = prev.down;
                        return;
                    } else {
                        //moet nod doen
                        //soek die posisie van die dae
                        Item nodeDay = rootDay[d];
                        Item prevDay = null;
                        Item nodeMonth = rootMonth[m];
                        Item prevMonth = null;
                        //kry die nodeMonth
                        while (nodeDay != null) {
                            //System.out.println("hier");
                            if (nodeDay.month > m) {
                                add.right = nodeDay;
                                add.back = null;
                                //kry nou die maand
                                //System.out.println(nodeMonth);
                                while (nodeMonth != null) {
                                    //System.out.println("hier");
                                    if (nodeMonth.day > d) {//sit voor in
                                        add.down = nodeMonth;
                                        if (nodeDay == rootDay[d]) {
                                            rootDay[d] = add;
                                            if (nodeMonth == rootMonth[m]) {
                                                rootMonth[m] = rootDay[d];
                                            } else {
                                                prevMonth.down = rootDay[d];
                                            }
                                            return;
                                        } else {
                                            prevDay.right = add;
                                            if (nodeMonth == rootMonth[m]) {
                                                rootMonth[m] = prevDay.right;
                                            } else {
                                                prevMonth.down = prevDay.right;
                                            }
                                            return;
                                        }
                                    }
                                    if (nodeMonth.day < d) {//traverse
                                        prevMonth = nodeMonth;
                                        nodeMonth = nodeMonth.down;
                                        //System.out.println(nodeMonth);
                                        continue;
                                    }
//---------------------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------------------
                                    //dink die is onnodig
                                    if (nodeMonth.day == day) {//priorities
                                        //System.out.println("hier");
                                    }
//---------------------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------------------
                                }//while vir maand kry
                                //System.out.println(nodeMonth);
                                //toets nou vir days[d]==nodeDay
                                if (rootDay[d] == nodeDay) {
                                    rootDay[d] = add;
                                    if (nodeMonth == rootMonth[m]) {
                                        //System.out.println("hier");
                                        rootMonth[m] = rootDay[d];
                                    } else {
                                        //System.out.println("hier");
                                        prevMonth.down = rootDay[d];
                                    }
                                    return;
                                } else {
                                    prevDay.right = add;
                                    if (nodeMonth == rootMonth[m]) {
                                        rootMonth[m] = prevDay.right;
                                    } else {
                                        prevMonth.down = prevDay.right;
                                    }
                                    return;

                                }
                            }
                            if (nodeDay.month < m) {
                                //System.out.println(1212);
                                prevDay = nodeDay;
                                nodeDay = nodeDay.right;
                                continue;
                            }
                            //--------------------------------------------------------------------
                            //--------------------------------------------------------------------
                            //----------------------werk nou van hier af--------------------------
                            //--------------------------------------------------------------------
                            //--------------------------------------------------------------------
                            if (nodeDay.month == m) {
                                //kry nou die maand
                                while (nodeMonth != null) {
                                    if (nodeMonth.day == d) {
                                        if (nodeDay.getPriority() < add.getPriority()) {
                                            add.right = nodeDay.right;
                                            add.down = nodeDay.down;//onseker oor die
                                            add.back = nodeDay;
                                            if (rootMonth[m] == nodeMonth) {
                                                rootMonth[m] = add;
                                                if (rootDay[d] == nodeDay) {
                                                    rootDay[d] = rootMonth[m];
                                                    return;
                                                } else {
                                                    prevDay.right = rootMonth[m];
                                                    return;
                                                }

                                            } else {
                                                prevMonth.down = add;
                                                if (rootDay[d] == nodeDay) {
                                                    rootDay[d] = prevMonth.down;
                                                    return;
                                                } else {
                                                    prevDay.right = prevMonth.down;
                                                    return;
                                                }
                                            }
                                        } else {
                                            Item terug = null;
                                            Item move = nodeMonth;
                                            add.right = null;
                                            add.down = null;
                                            while (move != null) {
                                                if (move.getPriority() < add.getPriority()) {
                                                    add.back = move;
                                                    terug.back = add;
                                                    return;
                                                }
                                                if (move.getPriority() == add.getPriority()) {
                                                    add.back = move.back;
                                                    move.back = add;
                                                    return;
                                                }
                                                if (move.getPriority() > add.getPriority()) {
                                                    terug = move;
                                                    move = move.back;
                                                }
                                            }
                                            terug.back = add;
                                            return;
                                        }
                                    }
                                    if (nodeMonth.day < d) {//traverse
                                        prevMonth = nodeMonth;
                                        nodeMonth = nodeMonth.down;
                                        continue;
                                    }
                                }//while om die month te kry
                            }
                            //--------------------------------------------------------------------
                            //--------------------------------------------------------------------
                            //--------------------------------------------------------------------
                            //--------------------------------------------------------------------
                            //--------------------------------------------------------------------
                            //insert laaste een hier
                        }//while
                        //
                        while (nodeMonth != null) {
                            //System.out.println("hier");
                            if (nodeMonth.day > d) {//sit voor in
                                add.down = nodeMonth;
                                if (nodeDay == rootDay[d]) {
                                    rootDay[d] = add;
                                    if (nodeMonth == rootMonth[m]) {
                                        rootMonth[m] = rootDay[d];
                                    } else {
                                        prevMonth.down = rootDay[d];
                                    }
                                    return;
                                } else {
                                    prevDay.right = add;
                                    if (nodeMonth == rootMonth[m]) {
                                        rootMonth[m] = prevDay.right;
                                    } else {
                                        prevMonth.down = prevDay.right;
                                    }
                                    return;
                                }
                            }
                            if (nodeMonth.day < d) {//traverse
                                prevMonth = nodeMonth;
                                nodeMonth = nodeMonth.down;
                                //System.out.println(nodeMonth);
                                continue;
                            }
                        }//while
                        //die sit dit aan die einde van n dag list
                        //onseker oor die
                        add.right = null;
                        add.right = null;
                        prevDay.right = add;
                        //System.out.println(prevMonth);
                        if (nodeMonth == rootMonth[m]) {
                            //System.out.println(121212);
                            rootMonth[m] = prevDay.right;
                            return;
                        }
                        prevMonth.down = prevDay.right;
                        return;
                        //----------------
                    }
                }
            }//--------------------------------------------------------------------
            //--------------------------------------------------------------------

        }//einde van Months[m]!= null
    }//end function
    }

}
