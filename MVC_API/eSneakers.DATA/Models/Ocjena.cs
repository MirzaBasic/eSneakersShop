using eSneakers.DATA.Helper;
using eSneakers.DATA.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace eSneakers.DATA.Models
{
    public class Ocjena:IEntity
    {
        public int Id { get; set; }

        public bool IsDeleted { get; set; }

        public virtual Korisnik Korisnik { get; set; }
        public int KorisnikId { get; set; }
        public virtual Proizvod Proizvod { get; set; }
        public int ProizvodId { get; set; }
        public DateTime DatumKreiranja { get; set; }

        public float OcjenaKorisnika { get; set; }
    }
}